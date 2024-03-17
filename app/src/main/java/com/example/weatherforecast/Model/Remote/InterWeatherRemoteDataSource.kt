package com.example.weatherforecast.Model.Remote

import com.example.weatherforecast.Model.CurrentWeather
import kotlinx.coroutines.flow.StateFlow

interface InterWeatherRemoteDataSource {
    suspend fun getWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String): StateFlow<CurrentWeather>
}