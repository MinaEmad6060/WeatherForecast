package com.example.weatherforecast.Model.Remote.Home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource: InterWeatherRemoteDataSource {

//    override suspend fun getAdditionalWeatherRemote(
//        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int): StateFlow<CurrentWeather> {
//        val stateFlow= MutableStateFlow(CurrentWeather())
//        stateFlow.emit(API.retrofitService.getAdditionalWeatherAPI(lat,lon,key,units,lang,cnt))
//        return stateFlow
//    }

    override suspend fun getAdditionalWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int
    ): Flow<CurrentWeather> = flow {
        val currentWeather = API.retrofitService.getAdditionalWeatherAPI(lat, lon, key, units, lang, cnt)
        emit(currentWeather)
    }
}