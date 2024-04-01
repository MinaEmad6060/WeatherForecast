package com.example.weatherforecast.Model.ViewModel.Fav

import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Repo.Fav.InterFavRepo
import com.example.weatherforecast.Model.Repo.FakeFavsLocalDataSource
import kotlinx.coroutines.flow.Flow


class FakeFavsRepo(
    private var fakeFavsLocalDataSource: FakeFavsLocalDataSource
) : InterFavRepo {
    override suspend fun getFavWeatherLocalRepo(): Flow<List<FavWeather>> {
        return fakeFavsLocalDataSource.getFavWeatherLocal()
    }

    override suspend fun deleteFavWeatherLocalRepo(favWeather: FavWeather): Int {
        return fakeFavsLocalDataSource.deleteFavWeatherLocal(favWeather)
    }

    override suspend fun insertFavWeatherLocalRepo(favWeather: FavWeather): Long {
        return fakeFavsLocalDataSource.insertFavWeatherLocal(favWeather)
    }
}