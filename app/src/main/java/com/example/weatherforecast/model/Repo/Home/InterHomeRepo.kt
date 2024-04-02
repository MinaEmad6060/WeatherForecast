package com.example.weatherforecast.Model.Repo.Home


import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Remote.Home.CurrentWeather
import kotlinx.coroutines.flow.Flow


interface InterHomeRepo {
    suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int
    ): Flow<CurrentWeather>

    suspend fun getAllHomeWeatherLocalRepo(): Flow<List<HomeWeather>>
    suspend fun deleteAllHomeWeatherLocalRepo(): Int
    suspend fun insertAllHomeWeatherLocalRepo(homeWeather: HomeWeather): Long

}