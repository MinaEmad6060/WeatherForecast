package com.example.weatherforecast

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherforecast.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var myMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSharedPreferences()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
            startActivity(Intent(this@MapsActivity, MainActivity::class.java))
            true
        }
    }
    private fun initSharedPreferences(){
        sharedPreferences =
            this.getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }


}