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

    suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String, cnt: Int
    ): Flow<CurrentWeather>


    suspend fun getAlertWeatherRemoteRepo(
        lat: Double, lon: Double, key: String
    ): StateFlow<OneCallAlert>



    suspend fun getAllHomeWeatherLocalRepo(context: Context): StateFlow<List<HomeWeather>>
    suspend fun deleteAllHomeWeatherLocalRepo(context: Context): Int
    suspend fun insertAllHomeWeatherLocalRepo(homeWeather: HomeWeather, context: Context): Long



    suspend fun getFavWeatherLocalRepo(context: Context): StateFlow<List<FavWeather>>
    suspend fun deleteFavWeatherLocalRepo(favWeather: FavWeather, context: Context): Int
    suspend fun insertFavWeatherLocalRepo(favWeather: FavWeather, context: Context): Long


    suspend fun getAlertWeatherLocalRepo(context: Context): StateFlow<List<AlertCalendar>>
    suspend fun deleteAlertWeatherLocalRepo(id: String, context: Context): Int
    suspend fun insertAlertWeatherLocalRepo(alertCalendar: AlertCalendar, context: Context): Long

}