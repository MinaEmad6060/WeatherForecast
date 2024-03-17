package com.example.weatherforecast.Model.Remote

import com.example.weatherforecast.Model.Clouds
import com.example.weatherforecast.Model.CurrentWeather
import com.example.weatherforecast.Model.Main
import com.example.weatherforecast.Model.Weather
import com.example.weatherforecast.Model.WeatherList
import com.example.weatherforecast.Model.Wind
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeatherRemoteDataSource:InterWeatherRemoteDataSource {
    override suspend fun getWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String): StateFlow<CurrentWeather> {
//        return API.retrofitService.getWeatherAPI(lat,lon,key,units,lang)
        val stateFlow= MutableStateFlow(CurrentWeather(
            weather = mutableListOf(Weather("", "")),
            main = Main(0.0, 0.0, 0.0, "", ""),
            wind = Wind(0.0),
            clouds = Clouds(0),
            name = "",
            time = "",
            date = "",
            list = mutableListOf(WeatherList(""))))
        stateFlow.emit(API.retrofitService.getWeatherAPI(lat,lon,key,units,lang))
        return stateFlow
    }
}