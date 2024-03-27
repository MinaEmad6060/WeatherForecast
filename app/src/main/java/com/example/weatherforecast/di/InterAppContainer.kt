package com.example.weatherforecast.di

import com.example.weatherforecast.Model.Local.Fav.FavLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.InterFavLocalDataSource

interface InterAppContainer {
    val favWeatherDAO: FavWeatherDAO
    val favLocalDataSource: InterFavLocalDataSource
}