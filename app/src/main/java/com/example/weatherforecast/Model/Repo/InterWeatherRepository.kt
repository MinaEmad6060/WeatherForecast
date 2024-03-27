package com.example.weatherforecast.Model.Repo

import android.content.Context
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
import com.example.weatherforecast.Model.Remote.Home.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface InterWeatherRepository {

//    suspend fun getAdditionalWeatherRemoteRepo(
//        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int
//    ): Flow<CurrentWeather>
//
//    suspend fun getAllHomeWeatherLocalRepo(context: Context): StateFlow<List<HomeWeather>>
//    suspend fun deleteAllHomeWeatherLocalRepo(context: Context): Int
//    suspend fun insertAllHomeWeatherLocalRepo(homeWeather: HomeWeather, context: Context): Long
//
}