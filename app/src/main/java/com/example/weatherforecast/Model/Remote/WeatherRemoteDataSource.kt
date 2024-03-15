package com.example.weatherforecast.Model.Remote

import com.example.weatherforecast.Model.CurrentWeather

class WeatherRemoteDataSource:InterWeatherRemoteDataSource {
    override suspend fun getWeatherRemote(
        lat: Double, lon: Double, key: String, units: String, lang: String): CurrentWeather {
        return API.retrofitService.getWeatherAPI(lat,lon,key,units,lang)
    }
}