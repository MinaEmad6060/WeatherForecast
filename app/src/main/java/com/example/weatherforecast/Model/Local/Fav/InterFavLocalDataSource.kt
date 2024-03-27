package com.example.weatherforecast.Model.Local.Fav

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

interface InterFavLocalDataSource {

    fun getRoomInstance(context: Context): FavWeatherDAO
    fun getFavWeatherLocal(context: Context): StateFlow<List<FavWeather>>
    suspend fun deleteFavWeatherLocal(favWeather: FavWeather, context: Context): Int
    suspend fun insertFavWeatherLocal(favWeather: FavWeather, context: Context): Long
}