package com.example.weatherforecast.Model.Local

import android.content.Context
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Local.Home.HomeWeatherDAO
import kotlinx.coroutines.flow.StateFlow

interface InterWeatherLocalDataSource {
    fun getRoomInstance(context: Context): HomeWeatherDAO
    fun getAllHomeWeatherLocal(context: Context): StateFlow<List<HomeWeather>>

    suspend fun deleteAllHomeWeatherLocal(context: Context): Int
    suspend fun insertAllHomeWeatherLocal(homeWeather: HomeWeather, context: Context): Long
}