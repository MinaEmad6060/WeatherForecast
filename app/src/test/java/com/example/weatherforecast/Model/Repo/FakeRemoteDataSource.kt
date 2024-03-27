package com.example.weatherforecast.Model.Repo

import com.example.weatherforecast.Model.Remote.Home.CurrentWeather
import com.example.weatherforecast.Model.Remote.Home.InterWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FakeRemoteDataSource: InterWeatherRemoteDataSource {
    override suspend fun getAdditionalWeatherRemote(
        lat: Double,
        lon: Double,
        key: String,
        units: String,
        lang: String,
        cnt: Int
    ): Flow<CurrentWeather> {
        TODO("Not yet implemented")
    }
}