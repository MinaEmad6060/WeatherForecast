package com.example.weatherforecast.Main

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
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.Alert.View.AlertFragment
import com.example.weatherforecast.Favourite.View.FavouriteFragment
import com.example.weatherforecast.Home.View.HomeFragment
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Model.Repo.Home.HomeRepo
import com.example.weatherforecast.R
import com.example.weatherforecast.Settings.View.SettingsFragment
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.di.AppContainer
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
    private lateinit var mainViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var mainViewModel: HomeFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MAINTEST", "Activity : onCreate")
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSharedPreferences()
        checkLocationAvailability()
        checkSaveOnInstance(savedInstanceState)
        initViewModel()
    }

    private fun checkSaveOnInstance(savedInstanceState: Bundle?){
        savedInstanceState ?: run {

            val lastFragmentTag = sharedPreferences.getString("lastFragmentTag", null)
            when (lastFragmentTag) {
                "HomeFragment" -> {
                    replaceFragment(HomeFragment(), "HomeFragment")
                    binding.bottomNav.selectedItemId = R.id.homeFragment
                    addNavigationListener()
                }
                "FavouriteFragment" -> {
                    replaceFragment(FavouriteFragment(), "FavouriteFragment")
                    binding.bottomNav.selectedItemId = R.id.favFragment
                    addNavigationListener()
                }
                "AlertFragment" -> {
                    replaceFragment(AlertFragment(), "AlertFragment")
                    binding.bottomNav.selectedItemId = R.id.alertFragment
                    addNavigationListener()
                }
                "SettingsFragment" -> {
                    replaceFragment(SettingsFragment(), "HomeFragment")
                    binding.bottomNav.selectedItemId = R.id.settingsFragment
                    addNavigationListener()
                }
                else ->  {
                    checkLocationAvailability()
                    replaceFragment(HomeFragment(), "HomeFragment")
                    binding.bottomNav.selectedItemId = R.id.homeFragment
                    addNavigationListener()
                }
            }
        }
    }

    private fun addNavigationListener(){
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment ->{
                    replaceFragment(HomeFragment(), "HomeFragment")
                    true }
                R.id.favFragment ->{
                    replaceFragment(FavouriteFragment(), "FavouriteFragment")
                    true}
                R.id.alertFragment -> {
                    replaceFragment(AlertFragment(), "AlertFragment")
                    true}
                R.id.settingsFragment ->{
                    replaceFragment(SettingsFragment(), "HomeFragment")
                    true}

                else -> {false}
            }
        }
    }

    private fun initViewModel(){
        mainViewModelFactory = (application as MyApplication).appContainer.homeFactory
        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory)
                .get(HomeFragmentViewModel::class.java)
    }

    private fun initSharedPreferences(){
        sharedPreferences =
            this.getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun replaceFragment(fragment: Fragment, fragTag: String){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frag_container,fragment)
        transaction.commit()
        editor.putString("lastFragmentTag", fragTag)
        editor.apply()
    }

    private fun checkLocationAvailability(){
        Log.i("MAINTEST", "checkLocationAvailability : onCreate")
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
        Log.i("MAINTEST", "getFreshLocation : onCreate")
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    Log.i("MAINTEST", "onLocationResult : onCreate")
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