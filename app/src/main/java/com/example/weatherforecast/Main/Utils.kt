package com.example.weatherforecast.Main

import android.content.Context
import android.content.SharedPreferences

class Utils {
    companion object{
        var lat=0.0
        var lon=0.0
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor

        var language="EN"

        fun initGeneralSharedPreferences(context: Context){
            sharedPreferences =
                context.getSharedPreferences("locationDetails", Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()
//            backGroundDesc = sharedPreferences.getString("backGround", "")!!
            language = sharedPreferences.getString("languageSettings", "EN")!!.toLowerCase()
        }
    }
}