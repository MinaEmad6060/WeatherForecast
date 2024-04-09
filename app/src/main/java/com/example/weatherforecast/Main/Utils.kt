package com.example.weatherforecast.Main

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.util.Log
import android.widget.VideoView
import com.example.weatherforecast.R
import com.example.weatherforecast.Settings.ViewModel.CentralSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class Utils {
    companion object{
        var lat=0.0
        var lon=0.0
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor

        var language=""
        var backGroundDesc = "09d"

        var radioGroupBtn = ""


        fun initBackGround(backGroundDesc: String, activity: Activity){
            var videoCode=0
            videoCode = when(backGroundDesc){
                "01d","01n" -> { R.raw.clear_sky }
                "02d","02n","03d","03n","04d","04n" -> { R.raw.cloudy }
                "09d","09n","10d","10n" -> { R.raw.rain }
                "13d","13n" -> { R.raw.snow }
                "11d","11n" -> { R.raw.thunder }
                "50d","50n" -> { R.raw.mist_def }
                else -> { R.raw.splash_video }
            }

            initVideo(videoCode,activity)
        }


        fun initVideo(raw: Int, activity: Activity){
            val video=activity.findViewById<VideoView>(R.id.home_video)
            val path = "android.resource://com.example.weatherforecast/" + raw
            val uri = Uri.parse(path)
            video.setVideoURI(uri)
            video.start()
            video.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true
                mediaPlayer.setVolume(0f, 0f)
            }
        }
        fun isNetworkConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

        fun createCentralSharedLanguage(externalScope: CoroutineScope,myResources: Resources):String{
            var result=""
            val centralSharedFlow= CentralSharedFlow(externalScope)
            externalScope.launch {
                centralSharedFlow.languageFlow.collectLatest {
                    result=it
                }
            }
            setLocale(result, myResources)
            return result
        }

        fun setLocale(languageCode: String, myResources: Resources) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val resources = myResources
            val configuration = resources.configuration
            configuration.setLocale(locale)

            @Suppress("DEPRECATION")
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

        fun setNumberLocale(number: Int, unit: String):String{
            val locale = Locale.getDefault()
            val numberFormat = NumberFormat.getInstance(locale)
            val tempFormat = numberFormat.format(number)
            return "$tempFormat $unit"
        }

    }
}