package com.example.weatherforecast.Model

import android.content.Context
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Local.InterWeatherLocalDataSource
import com.example.weatherforecast.Model.Local.WeatherLocalDataSource
import com.example.weatherforecast.Model.Remote.InterWeatherRemoteDataSource
import com.example.weatherforecast.Model.Remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.StateFlow

object WeatherRepository : InterWeatherRepository {

    private var remoteWeather: InterWeatherRemoteDataSource = WeatherRemoteDataSource()
    private var roomWeather: InterWeatherLocalDataSource = WeatherLocalDataSource()

    override suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String,cnt: Int) =
        remoteWeather.getAdditionalWeatherRemote(lat,lon,key,units,lang,cnt)



    override suspend fun getAllHomeWeatherLocalRepo(context: Context): StateFlow<List<HomeWeather>>{
        return roomWeather.getAllHomeWeatherLocal(context)
    }

    override suspend fun deleteAllHomeWeatherLocalRepo(context: Context): Int{
        return roomWeather.deleteAllHomeWeatherLocal(context)
    }

    override suspend fun insertAllHomeWeatherLocalRepo(homeWeather: HomeWeather, context: Context): Long{
        return roomWeather.insertAllHomeWeatherLocal(homeWeather,context)
    }


}