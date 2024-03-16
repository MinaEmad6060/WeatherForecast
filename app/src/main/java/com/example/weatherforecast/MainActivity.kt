package com.example.weatherforecast

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weatherforecast.Alert.View.AlertFragment
import com.example.weatherforecast.Favourite.View.FavouriteFragment
import com.example.weatherforecast.Home.View.HomeFragment
import com.example.weatherforecast.Settings.View.SettingsFragment
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

const val REQUEST_LOCATION_CODE=100

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savedInstanceState ?: replaceFragment(HomeFragment())
        initSharedPreferences()
        checkLocationAvailability()
        addNavigationListener()
    }

    private fun addNavigationListener(){
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment ->{
                    replaceFragment(HomeFragment())
                    true }
                R.id.favFragment ->{
                    replaceFragment(FavouriteFragment())
                    true}
                R.id.alertFragment -> {
                    replaceFragment(AlertFragment())
                    true}
                R.id.settingsFragment ->{
                    replaceFragment(SettingsFragment())
                    true}

                else -> {false}
            }
        }
    }

    private fun initSharedPreferences(){
        sharedPreferences =
            this.getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        // Retrieve a value from the shared preferences
        // val value = sharedPreferences.getString("key", "default_value")
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frag_container,fragment)
        transaction.commit()
    }

    private fun checkLocationAvailability(){
        if(checkPermission()){
            if(isLocationEnabled()) getFreshLocation()
            else enableLocationServices()
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_LOCATION_CODE){
            if(grantResults.size > 1 && grantResults.get(0)
                == PackageManager.PERMISSION_GRANTED){
                getFreshLocation()
            }
        }
    }


    private fun checkPermission() : Boolean{
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager : LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    editor.putString("latitude", location?.latitude.toString())
                    editor.putString("longitude", location?.longitude.toString())
                    editor.apply()
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }, Looper.myLooper()
        )
    }

    private fun enableLocationServices(){
        Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }



}