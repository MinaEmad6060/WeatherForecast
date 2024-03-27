package com.example.weatherforecast.di

import android.content.Context
import com.example.weatherforecast.Model.Local.Alert.AlertLocalDataSource
import com.example.weatherforecast.Model.Local.Alert.AlertWeatherDAO
import com.example.weatherforecast.Model.Local.Alert.InterAlertLocalDataSource
import com.example.weatherforecast.Model.Local.Alert.dbAlert
import com.example.weatherforecast.Model.Local.Fav.FavLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.dbFav
import com.example.weatherforecast.Model.Remote.Alert.AlertAPI
import com.example.weatherforecast.Model.Remote.Alert.AlertRemoteDataSource
import com.example.weatherforecast.Model.Remote.Alert.InterAlertRemoteDataSource
import com.example.weatherforecast.Model.Remote.Alert.RetrofitHelper

class AppContainer(context: Context, ): InterAppContainer {
    override val favWeatherDAO: FavWeatherDAO by lazy {
        val database: dbFav = dbFav.getInstance(context)
        val dao = database.getFavWeatherDao()
        dao
    }
    override val favLocalDataSource: FavLocalDataSource by lazy {
        FavLocalDataSource(favWeatherDAO)
    }

    override val alertWeatherDAO: AlertWeatherDAO by lazy {
        val database: dbAlert = dbAlert.getInstance(context)
        val dao = database.getAlertWeatherDao()
        dao
    }
    override val alertWeatherAPI: AlertAPI by lazy {
        RetrofitHelper.retrofitInstance.create(AlertAPI::class.java)
    }
    override val alertWeatherLocalDataSource: InterAlertLocalDataSource by lazy {
        AlertLocalDataSource(alertWeatherDAO)
    }
    override val alertWeatherRemoteDataSource: InterAlertRemoteDataSource by lazy {
        AlertRemoteDataSource(alertWeatherAPI)
    }
}