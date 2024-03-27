package com.example.weatherforecast.Main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Model.Repo.Home.HomeRepo
import com.example.weatherforecast.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherforecast.databinding.ActivityMapsBinding
import com.example.weatherforecast.di.AppContainer

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = "MapsActivity"
    private lateinit var myMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var selectedFragment: String
    private lateinit var mainViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var mainViewModel: HomeFragmentViewModel
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainer(applicationContext)

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
                editor.putString("goToFragment","Home")
                editor.apply()
            }else if(selectedFragment == "Fav"){
                editor.putString("goToFragment","Fav")
                editor.apply()
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
        mainViewModelFactory = appContainer.homeFactory
        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }

}