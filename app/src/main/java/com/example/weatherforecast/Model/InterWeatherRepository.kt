package com.example.weatherforecast.Model

interface InterWeatherRepository {
    suspend fun getAllWeatherRemoteRepo(): CurrentWeather
}