package com.example.weatherforecast.Model.Repo.FavTest

import android.content.Context
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.InterFavLocalDataSource
import kotlinx.coroutines.flow.StateFlow

class FakeFavLocalDataSource(
    private var favList: MutableList<FavWeather> = mutableListOf()
): InterFavLocalDataSource {

    override fun getFavWeatherLocal(): StateFlow<List<FavWeather>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavWeatherLocal(favWeather: FavWeather): Int {
        return if(favList.remove(favWeather)) 1 else 0
    }

    override suspend fun insertFavWeatherLocal(favWeather: FavWeather): Long {
        return if(favList.add(favWeather)) 1 else 0
    }
}