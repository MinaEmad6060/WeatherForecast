package com.example.weatherforecast

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Favourite.View.FavouriteFragment
import com.example.weatherforecast.Home.View.HomeFragment
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Model.WeatherRepository

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherforecast.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = "MapsActivity"
    private lateinit var myMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var selectedFragment: String


    private lateinit var mainViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var mainViewModel: HomeFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSharedPreferences()
        initViewModel()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        myMap.setOnMapClickListener { latLng ->
            myMap.clear()
            myMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7f))
            editor.putString("latitude", latLng.latitude.toString())
            editor.putString("longitude", latLng.longitude.toString())
            editor.apply()
            val selectedLatitude = latLng.latitude
            val selectedLongitude = latLng.longitude
            Toast.makeText(
                this, "Latitude = $selectedLatitude & Longitude = $selectedLongitude",
                Toast.LENGTH_LONG
            ).show()
        }
        myMap.setOnMarkerClickListener {
            if(selectedFragment == "Home"){
                mainViewModel.navigateToFragment("Home")
//                startActivity(Intent(this@MapsActivity, MainActivity::class.java))
            }else if(selectedFragment == "Fav"){
                mainViewModel.navigateToFragment("Fav")
//                startActivity(Intent(this@MapsActivity, FavouriteFragment::class.java))
            }
            startActivity(Intent(this@MapsActivity, MainActivity::class.java))

            true
        }
    }
    private fun initSharedPreferences(){
        sharedPreferences =
            this.getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        selectedFragment = sharedPreferences.getString("SelectedFragment", "").toString()
        Log.i(TAG, "initSharedPreferences: $selectedFragment")
    }


    private fun initViewModel(){
        mainViewModelFactory = HomeFragmentViewModelFactory(WeatherRepository)
        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }


    fun navigateToSpecificFragment() {

    }

}