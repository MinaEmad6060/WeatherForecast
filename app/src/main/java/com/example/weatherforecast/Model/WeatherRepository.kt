package com.example.weatherforecast.Model

import com.example.weatherforecast.Model.Remote.InterWeatherRemoteDataSource
import com.example.weatherforecast.Model.Remote.WeatherRemoteDataSource

object WeatherRepository : InterWeatherRepository {

    private var remoteWeather: InterWeatherRemoteDataSource = WeatherRemoteDataSource()
//    private var roomWeather: InterWeatherLocalDataSource = WeatherLocalDataSource()

    override suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String,cnt: Int) =
        remoteWeather.getAdditionalWeatherRemote(lat,lon,key,units,lang,cnt)

}