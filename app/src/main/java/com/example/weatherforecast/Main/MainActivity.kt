package com.example.weatherforecast.Main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.weatherforecast.Alert.View.AlertFragment
import com.example.weatherforecast.Favourite.View.FavouriteFragment
import com.example.weatherforecast.Home.View.HomeFragment
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModel
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Main.Utils.Companion.editor
import com.example.weatherforecast.Main.Utils.Companion.sharedPreferences
import com.example.weatherforecast.R
import com.example.weatherforecast.Settings.View.SettingsFragment
import com.example.weatherforecast.Settings.ViewModel.CentralSharedFlow
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Locale

const val REQUEST_LOCATION_CODE=100

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val backGround ="android.resource://com.example.weatherforecast/"
    private var backGroundDesc =""
    private lateinit var binding: ActivityMainBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mainViewModelFactory: HomeFragmentViewModelFactory
    private lateinit var mainViewModel: HomeFragmentViewModel
    private lateinit var centralSharedFlow: CentralSharedFlow
    private lateinit var externalScope: CoroutineScope
    private lateinit var job: Job
    private var mainLanguage =""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MAINTEST", "Activity : onCreate")
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSharedPreferences()
        checkLocationAvailability()
        checkSaveOnInstance(savedInstanceState)
        initViewModel()

        externalScope= lifecycleScope
        centralSharedFlow=CentralSharedFlow(externalScope)
        job = externalScope.launch {
            centralSharedFlow.tickFlow.collect {
                Log.i("newShare", "main: $it")
                mainLanguage=it
            }
        }

        setLocale(mainLanguage)
    }

//    override fun onDestroy() {
//        super.onDestroy()
////        binding.mainVideo.stopPlayback()
//        externalScope.cancel()
//        job.cancel()
//        centralSharedFlow.cleanup()
//
//    }

//    fun initBackGround(){
//        Log.i(TAG, "initBackGround: $backGroundDesc")
//        when(backGroundDesc){
//            "01d","02d" -> {
//                Log.i(TAG, "initBackGroundEnter: $backGroundDesc")
//                initVideo(R.raw.splash_video)}
//            else -> {initVideo(R.raw.splash_video)}
//        }
//    }


//    fun initVideo(raw: Int){
//        val path = backGround + raw
//        val uri = Uri.parse(path)
//        binding.mainVideo.setVideoURI(uri)
//        binding.mainVideo.start()
//        binding.mainVideo.setOnPreparedListener { mediaPlayer ->
//            mediaPlayer.isLooping = true
//        }
//    }

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
        backGroundDesc = sharedPreferences.getString("backGround", "")!!
        mainLanguage = sharedPreferences.getString("languageSettings", "EN")!!.toLowerCase()
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

    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)

        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

}