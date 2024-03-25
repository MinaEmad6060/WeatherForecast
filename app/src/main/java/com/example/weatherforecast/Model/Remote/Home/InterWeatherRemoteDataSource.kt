package com.example.weatherforecast.Model.Remote.Home

import kotlinx.coroutines.flow.StateFlow

interface InterWeatherRemoteDataSource {

    suspend fun getAdditionalWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int): StateFlow<CurrentWeather>
}