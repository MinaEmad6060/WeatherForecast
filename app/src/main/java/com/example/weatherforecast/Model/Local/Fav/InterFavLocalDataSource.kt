package com.example.weatherforecast.Model.Local.Fav

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface InterFavLocalDataSource {
    fun getFavWeatherLocal(): Flow<List<FavWeather>>
    suspend fun deleteFavWeatherLocal(favWeather: FavWeather): Int
    suspend fun insertFavWeatherLocal(favWeather: FavWeather): Long
}