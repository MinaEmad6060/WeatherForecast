package com.example.weatherforecast.Model

import kotlinx.coroutines.flow.StateFlow

interface InterWeatherRepository {
    suspend fun getWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String
    ): StateFlow<CurrentWeather>

    suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String,cnt: Int
    ): StateFlow<CurrentWeather>
}