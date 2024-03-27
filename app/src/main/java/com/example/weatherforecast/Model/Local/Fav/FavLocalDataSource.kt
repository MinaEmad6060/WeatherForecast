package com.example.weatherforecast.Model.Local.Fav

import android.content.Context
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavLocalDataSource(private val dao: FavWeatherDAO): InterFavLocalDataSource {


    override fun getFavWeatherLocal(): Flow<List<FavWeather>> {
        return dao.getFavWeather()
    }

    override suspend fun deleteFavWeatherLocal(favWeather: FavWeather): Int {
        return dao.delete(favWeather)
    }

    override suspend fun insertFavWeatherLocal(favWeather: FavWeather): Long {
        return dao.insert(favWeather)
    }

}