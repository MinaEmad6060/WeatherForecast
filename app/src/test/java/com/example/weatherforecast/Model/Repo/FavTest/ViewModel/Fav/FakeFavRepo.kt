package com.example.weatherforecast.Model.Repo.FavTest.ViewModel.Fav

import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Repo.Fav.InterFavRepo
import com.example.weatherforecast.Model.Repo.FavTest.Repo.FakeFavLocalDataSource
import kotlinx.coroutines.flow.Flow


class FakeFavRepo(
    private var fakeFavLocalDataSource: FakeFavLocalDataSource
) : InterFavRepo {
    override suspend fun getFavWeatherLocalRepo(): Flow<List<FavWeather>> {
        return fakeFavLocalDataSource.getFavWeatherLocal()
    }

    override suspend fun deleteFavWeatherLocalRepo(favWeather: FavWeather): Int {
        return fakeFavLocalDataSource.deleteFavWeatherLocal(favWeather)
    }

    override suspend fun insertFavWeatherLocalRepo(favWeather: FavWeather): Long {
        return fakeFavLocalDataSource.insertFavWeatherLocal(favWeather)
    }
}