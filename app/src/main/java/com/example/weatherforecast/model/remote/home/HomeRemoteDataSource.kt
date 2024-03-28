package com.example.weatherforecast.Model.Remote.Home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRemoteDataSource(private val homeAPI: HomeAPI): InterRemoteDataSource {

    override suspend fun getAdditionalWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int
    ): Flow<CurrentWeather> = flow {
        val currentWeather = homeAPI.getAdditionalWeatherAPI(lat, lon, key, units, lang, cnt)
        emit(currentWeather)
    }
}