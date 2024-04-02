package com.example.weatherforecast.Model.Repo.Fav


import com.example.weatherforecast.Model.Local.Fav.FavWeather
import kotlinx.coroutines.flow.Flow

interface InterFavRepo {
    suspend fun getFavWeatherLocalRepo(): Flow<List<FavWeather>>
    suspend fun deleteFavWeatherLocalRepo(favWeather: FavWeather): Int
    suspend fun insertFavWeatherLocalRepo(favWeather: FavWeather): Long
}