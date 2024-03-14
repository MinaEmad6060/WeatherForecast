package com.example.weatherforecast.Model.Remote

import com.example.weatherforecast.Model.CurrentWeather

interface InterWeatherRemoteDataSource {
    suspend fun getAllWeatherRemote(): CurrentWeather
}