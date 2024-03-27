package com.example.weatherforecast.Model.Repo

import android.content.Context
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
import com.example.weatherforecast.Model.Remote.Home.CurrentWeather
import com.example.weatherforecast.Model.Repo.FavTest.Repo.FakeFavLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class FakeRepo(
    private var fakeFavLocalDataSource: FakeFavLocalDataSource
//    private var fakeFavRemoteDataSource: FakeRemoteDataSource,
//    private var fakeAlertLocalDataSource: FakeAlertLocalDataSource,
//    private var fakeAlertRemoteDataSource: FakeAlertRemoteDataSource
): InterWeatherRepository {

    var favList= mutableListOf(FavWeather())

    override suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double,
        lon: Double,
        key: String,
        units: String,
        lang: String,
        cnt: Int
    ): Flow<CurrentWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlertWeatherRemoteRepo(
        lat: Double,
        lon: Double,
        key: String
    ): StateFlow<OneCallAlert> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllHomeWeatherLocalRepo(context: Context): StateFlow<List<HomeWeather>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllHomeWeatherLocalRepo(context: Context): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllHomeWeatherLocalRepo(
        homeWeather: HomeWeather,
        context: Context
    ): Long {
        TODO("Not yet implemented")
    }

//    override suspend fun getFavWeatherLocalRepo(context: Context): StateFlow<List<FavWeather>> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteFavWeatherLocalRepo(favWeather: FavWeather, context: Context): Int {
//        return fakeFavLocalDataSource.deleteFavWeatherLocal(favWeather,context)
//    }
//
//    override suspend fun insertFavWeatherLocalRepo(favWeather: FavWeather, context: Context): Long {
//        return fakeFavLocalDataSource.insertFavWeatherLocal(favWeather,context)
//    }

    override suspend fun getAlertWeatherLocalRepo(context: Context): StateFlow<List<AlertCalendar>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertWeatherLocalRepo(id: String, context: Context): Int {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlertWeatherLocalRepo(
        alertCalendar: AlertCalendar,
        context: Context
    ): Long {
        TODO("Not yet implemented")
    }
}