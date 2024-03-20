package com.example.weatherforecast.Model

import android.content.Context
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Remote.CurrentWeather
import kotlinx.coroutines.flow.StateFlow

interface InterWeatherRepository {
    suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int
    ): StateFlow<CurrentWeather>



    suspend fun getAllHomeWeatherLocalRepo(context: Context): StateFlow<List<HomeWeather>>
    suspend fun deleteAllHomeWeatherLocalRepo(context: Context): Int
    suspend fun insertAllHomeWeatherLocalRepo(homeWeather: HomeWeather, context: Context): Long



    suspend fun getFavWeatherLocalRepo(context: Context): StateFlow<List<FavWeather>>
    suspend fun deleteFavWeatherLocalRepo(favWeather: FavWeather, context: Context): Int
    suspend fun deleteAllFavWeatherLocalRepo(context: Context): Int
    suspend fun insertFavWeatherLocalRepo(favWeather: FavWeather, context: Context): Long



}