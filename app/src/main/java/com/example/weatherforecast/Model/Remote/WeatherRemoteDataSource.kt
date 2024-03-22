package com.example.weatherforecast.Model.Remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeatherRemoteDataSource:InterWeatherRemoteDataSource {

    override suspend fun getAdditionalWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int): StateFlow<CurrentWeather> {
        val stateFlow= MutableStateFlow(CurrentWeather())
        stateFlow.emit(API.retrofitService.getAdditionalWeatherAPI(lat,lon,key,units,lang,cnt))
        return stateFlow
    }
}