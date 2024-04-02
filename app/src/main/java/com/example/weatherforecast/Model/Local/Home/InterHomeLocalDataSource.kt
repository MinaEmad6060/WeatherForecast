package com.example.weatherforecast.Model.Local.Home

import kotlinx.coroutines.flow.Flow

interface InterHomeLocalDataSource {
    fun getAllHomeWeatherLocal(): Flow<List<HomeWeather>>

    suspend fun deleteAllHomeWeatherLocal(): Int
    suspend fun insertAllHomeWeatherLocal(homeWeather: HomeWeather): Long
}