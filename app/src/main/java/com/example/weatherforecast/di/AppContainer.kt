package com.example.weatherforecast.di

import android.content.Context
import com.example.weatherforecast.Alert.ViewModel.AlertFragmentViewModelFactory
import com.example.weatherforecast.Favourite.ViewModel.FavFragmentViewModelFactory
import com.example.weatherforecast.Home.ViewModel.HomeFragmentViewModelFactory
import com.example.weatherforecast.Model.Local.Alert.AlertLocalDataSource
import com.example.weatherforecast.Model.Local.Alert.AlertWeatherDAO
import com.example.weatherforecast.Model.Local.Alert.InterAlertLocalDataSource
import com.example.weatherforecast.Model.Local.Alert.dbAlert
import com.example.weatherforecast.Model.Local.Fav.FavLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeatherDAO
import com.example.weatherforecast.Model.Local.Fav.dbFav
import com.example.weatherforecast.Model.Local.Home.HomeLocalDataSource
import com.example.weatherforecast.Model.Local.Home.HomeWeatherDAO
import com.example.weatherforecast.Model.Local.Home.InterHomeLocalDataSource
import com.example.weatherforecast.Model.Local.Home.dbHome
//import com.example.weatherforecast.Model.Remote.Alert.AlertRemoteDataSource
//import com.example.weatherforecast.Model.Remote.Alert.InterAlertRemoteDataSource
import com.example.weatherforecast.Model.Remote.Home.InterRemoteDataSource
import com.example.weatherforecast.Model.Repo.Alert.AlertRepo
import com.example.weatherforecast.Model.Repo.Alert.InterAlertRepo
import com.example.weatherforecast.Model.Repo.Fav.FavRepo
import com.example.weatherforecast.Model.Repo.Fav.InterFavRepo
import com.example.weatherforecast.Model.Repo.Home.HomeRepo
import com.example.weatherforecast.model.Remote.ApiRetrofitHelper
import com.example.weatherforecast.model.Remote.ApiService
import com.example.weatherforecast.model.Remote.Home.HomeRemoteDataSource


class AppContainer(context: Context): InterAppContainer {



    //fav
    override val favWeatherDAO: FavWeatherDAO by lazy {
        val database: dbFav = dbFav.getInstance(context)
        val dao = database.getFavWeatherDao()
        dao
    }
    override val favLocalDataSource: FavLocalDataSource by lazy {
        FavLocalDataSource(favWeatherDAO)
    }
    override val favRepo: InterFavRepo by lazy {
        FavRepo(favLocalDataSource)
    }

    override val favFactory: FavFragmentViewModelFactory by lazy {
        FavFragmentViewModelFactory(favRepo)
    }

    //alert
    override val alertWeatherDAO: AlertWeatherDAO by lazy {
        val database: dbAlert = dbAlert.getInstance(context)
        val dao = database.getAlertWeatherDao()
        dao
    }

    override val alertWeatherLocalDataSource: InterAlertLocalDataSource by lazy {
        AlertLocalDataSource(alertWeatherDAO)
    }
//    override val alertWeatherRemoteDataSource: InterAlertRemoteDataSource by lazy {
//        AlertRemoteDataSource(apiService)
//    }
    override val alertRepo: InterAlertRepo by lazy {
        AlertRepo(
//            alertWeatherRemoteDataSource
            alertWeatherLocalDataSource
        )
    }
    override val alertFactory: AlertFragmentViewModelFactory by lazy {
        AlertFragmentViewModelFactory(alertRepo)
    }

    //home
    override val homeWeatherDAO: HomeWeatherDAO by lazy {
        val database: dbHome = dbHome.getInstance(context)
        val dao = database.getHomeWeatherDao()
        dao
    }

    override val homeWeatherLocalDataSource: InterHomeLocalDataSource by lazy {
        HomeLocalDataSource(homeWeatherDAO)
    }
    override val homeWeatherRemoteDataSource: InterRemoteDataSource by lazy {
        HomeRemoteDataSource(apiService)
    }
    override val homeRepo: HomeRepo by lazy {
        HomeRepo(
            homeWeatherRemoteDataSource,
            homeWeatherLocalDataSource
        )
    }
    override val homeFactory: HomeFragmentViewModelFactory by lazy {
        HomeFragmentViewModelFactory(homeRepo)
    }

    override val apiService: ApiService by lazy {
        ApiRetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }

}