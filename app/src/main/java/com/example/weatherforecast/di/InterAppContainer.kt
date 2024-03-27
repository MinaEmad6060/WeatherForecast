package com.example.weatherforecast.di

import com.example.weatherforecast.Model.Local.Alert.AlertLocalDataSource
import com.example.weatherforecast.Model.Local.Alert.AlertWeatherDAO
import com.example.weatherforecast.Model.Local.Alert.InterAlertLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.InterFavLocalDataSource
import com.example.weatherforecast.Model.Remote.Alert.AlertAPI
import com.example.weatherforecast.Model.Remote.Alert.InterAlertRemoteDataSource

interface InterAppContainer {
    val favWeatherDAO: FavWeatherDAO
    val favLocalDataSource: InterFavLocalDataSource
    val alertWeatherDAO: AlertWeatherDAO
    val alertWeatherAPI: AlertAPI
    val alertWeatherLocalDataSource: InterAlertLocalDataSource
    val alertWeatherRemoteDataSource: InterAlertRemoteDataSource
}