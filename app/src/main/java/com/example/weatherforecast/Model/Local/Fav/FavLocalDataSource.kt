package com.example.weatherforecast.Model.Local.Fav

import kotlinx.coroutines.flow.Flow

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