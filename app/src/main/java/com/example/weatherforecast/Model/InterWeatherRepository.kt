package com.example.weatherforecast.Model

interface InterWeatherRepository {
    suspend fun getWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String
    ): CurrentWeather
}