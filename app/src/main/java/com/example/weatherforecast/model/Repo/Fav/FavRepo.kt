package com.example.weatherforecast.Model.Repo.Fav


import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Fav.InterFavLocalDataSource
import kotlinx.coroutines.flow.Flow

class FavRepo(private val roomFavWeather: InterFavLocalDataSource): InterFavRepo {
    override suspend fun getFavWeatherLocalRepo(): Flow<List<FavWeather>> {
        return roomFavWeather.getFavWeatherLocal()
    }

    override suspend fun deleteFavWeatherLocalRepo(favWeather: FavWeather): Int {
        return roomFavWeather.deleteFavWeatherLocal(favWeather)
    }


    override suspend fun insertFavWeatherLocalRepo(favWeather: FavWeather): Long {
        return roomFavWeather.insertFavWeatherLocal(favWeather)
    }
}