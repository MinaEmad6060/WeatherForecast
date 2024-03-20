package com.example.weatherforecast.Model.Local.Home

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

interface InterHomeLocalDataSource {
    fun getRoomInstance(context: Context): HomeWeatherDAO
    fun getAllHomeWeatherLocal(context: Context): StateFlow<List<HomeWeather>>

    suspend fun deleteAllHomeWeatherLocal(context: Context): Int
    suspend fun insertAllHomeWeatherLocal(homeWeather: HomeWeather, context: Context): Long
}