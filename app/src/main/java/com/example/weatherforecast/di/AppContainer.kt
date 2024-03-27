package com.example.weatherforecast.di

import android.content.Context
import com.example.weatherforecast.Model.Local.Fav.FavLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.dbFav

class AppContainer(context: Context): InterAppContainer {
    override val favWeatherDAO: FavWeatherDAO by lazy {
        val database: dbFav = dbFav.getInstance(context)
        val dao = database.getFavWeatherDao()
        dao
    }
    override val favLocalDataSource: FavLocalDataSource by lazy {
        FavLocalDataSource(favWeatherDAO)
    }
}