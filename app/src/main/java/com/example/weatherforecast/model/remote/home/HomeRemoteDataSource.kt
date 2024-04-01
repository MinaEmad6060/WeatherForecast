package com.example.weatherforecast.Model.Remote.Home


import com.example.weatherforecast.model.Remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class HomeRemoteDataSource(private val homeAPI: ApiService): InterRemoteDataSource
{

    override suspend fun getAdditionalWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int
    ): Flow<CurrentWeather> = flow {
        val currentWeather = homeAPI.getAdditionalWeatherAPI(lat, lon, key, units, lang, cnt)
        emit(currentWeather)
    }
}