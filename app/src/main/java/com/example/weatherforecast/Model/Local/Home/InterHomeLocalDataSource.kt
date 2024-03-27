package com.example.weatherforecast.Model.Local.Home

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface InterHomeLocalDataSource {
    fun getAllHomeWeatherLocal(): Flow<List<HomeWeather>>

    suspend fun deleteAllHomeWeatherLocal(): Int
    suspend fun insertAllHomeWeatherLocal(homeWeather: HomeWeather): Long
}