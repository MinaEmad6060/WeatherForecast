package com.example.weatherforecast.Main

import android.app.Application
import com.example.weatherforecast.di.AppContainer
import com.example.weatherforecast.di.InterAppContainer

class MyApplication: Application() {

    lateinit var appContainer: InterAppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}