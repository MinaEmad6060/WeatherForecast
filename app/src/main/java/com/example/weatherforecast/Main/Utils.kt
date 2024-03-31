package com.example.weatherforecast.Main

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.example.weatherforecast.Settings.ViewModel.CentralSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class Utils {
    companion object{
        var lat=0.0
        var lon=0.0
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor

        var language=""

        fun isNetworkConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

        fun createCentralSharedLanguage(externalScope: CoroutineScope,myResources: Resources):String{
            var result=""
            val centralSharedFlow= CentralSharedFlow(externalScope)
            externalScope.launch {
                centralSharedFlow.tickFlow.collectLatest {
                    Log.i("newShare", "main: $it")
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

//        fun initGeneralSharedPreferences(context: Context){
//            sharedPreferences =
//                context.getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
//            editor = sharedPreferences.edit()
////            backGroundDesc = sharedPreferences.getString("backGround", "")!!
//            language = sharedPreferences.getString("languageSettings", "EN")!!.toLowerCase()
//        }
    }
}