package com.example.weatherforecast.Model.Local.Fav

import kotlinx.coroutines.flow.Flow

interface InterFavLocalDataSource {
    fun getFavWeatherLocal(): Flow<List<FavWeather>>
    suspend fun deleteFavWeatherLocal(favWeather: FavWeather): Int
    suspend fun insertFavWeatherLocal(favWeather: FavWeather): Long
}