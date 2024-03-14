package com.example.weatherforecast.Model

import com.example.weatherforecast.Model.Remote.InterWeatherRemoteDataSource
import com.example.weatherforecast.Model.Remote.WeatherRemoteDataSource

object WeatherRepository : InterWeatherRepository {

    private var remoteWeather: InterWeatherRemoteDataSource = WeatherRemoteDataSource()
//    private var roomWeather: InterWeatherLocalDataSource = WeatherLocalDataSource()


    override suspend fun getAllWeatherRemoteRepo() =
        remoteWeather.getAllWeatherRemote()

}