package com.example.weatherforecast.Model.Repo.FavTest.Repo

import android.content.Context
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.InterFavLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeFavLocalDataSource(
    private var favList: MutableList<FavWeather> = mutableListOf()
): InterFavLocalDataSource {

    override fun getFavWeatherLocal(): Flow<List<FavWeather>> = flow {
        emit(favList.toList())
    }

    override suspend fun deleteFavWeatherLocal(favWeather: FavWeather): Int {
        val removed = favList.removeAll { it.cityName == favWeather.cityName }
        return if (removed) 1 else 0
    }

    override suspend fun insertFavWeatherLocal(favWeather: FavWeather): Long {
        return if(favList.add(favWeather)) 1 else 0
    }
}