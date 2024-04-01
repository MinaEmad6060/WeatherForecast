package com.example.weatherforecast.di

import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModelFactory
import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModelFactory
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Model.Local.Alert.AlertWeatherDAO
import com.example.weatherforecast.Model.Local.Alert.InterAlertLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.InterFavLocalDataSource
import com.example.weatherforecast.Model.Local.Home.HomeWeatherDAO
import com.example.weatherforecast.Model.Local.Home.InterHomeLocalDataSource
//import com.example.weatherforecast.Model.Remote.Alert.AlertAPI
import com.example.weatherforecast.Model.Remote.Alert.InterAlertRemoteDataSource
//import com.example.weatherforecast.Model.Remote.Home.HomeAPI
import com.example.weatherforecast.Model.Remote.Home.InterRemoteDataSource
import com.example.weatherforecast.Model.Repo.Alert.InterAlertRepo
import com.example.weatherforecast.Model.Repo.Fav.InterFavRepo
import com.example.weatherforecast.Model.Repo.Home.InterHomeRepo
import com.example.weatherforecast.model.Remote.ApiService

interface InterAppContainer {
    val favWeatherDAO: FavWeatherDAO
    val favLocalDataSource: InterFavLocalDataSource
    val favRepo: InterFavRepo
    val favFactory: FavFragmentViewModelFactory
    val alertWeatherDAO: AlertWeatherDAO
//    val alertWeatherAPI: AlertAPI
    val alertWeatherLocalDataSource: InterAlertLocalDataSource
    val alertWeatherRemoteDataSource: InterAlertRemoteDataSource
    val alertRepo: InterAlertRepo
    val alertFactory: AlertFragmentViewModelFactory
    val homeWeatherDAO: HomeWeatherDAO
//    val homeWeatherAPI: HomeAPI
    val homeWeatherLocalDataSource: InterHomeLocalDataSource
    val homeWeatherRemoteDataSource: InterRemoteDataSource
    val homeRepo: InterHomeRepo
    val homeFactory: HomeFragmentViewModelFactory

    val apiService: ApiService
}