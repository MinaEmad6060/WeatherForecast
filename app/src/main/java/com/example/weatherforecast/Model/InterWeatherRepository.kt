package com.example.weatherforecast.Model

import android.content.Context
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import kotlinx.coroutines.flow.StateFlow

interface InterWeatherRepository {
    suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int
    ): StateFlow<CurrentWeather>

    suspend fun getAllHomeWeatherLocalRepo(context: Context): StateFlow<List<HomeWeather>>
    suspend fun deleteAllHomeWeatherLocalRepo(context: Context): Int
    suspend fun insertAllHomeWeatherLocalRepo(homeWeather: HomeWeather, context: Context): Long

}