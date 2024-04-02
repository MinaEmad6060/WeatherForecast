package com.example.weatherforecast.Model.Remote.Home

import kotlinx.coroutines.flow.Flow

interface InterRemoteDataSource {
    suspend fun getAdditionalWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int): Flow<CurrentWeather>
}