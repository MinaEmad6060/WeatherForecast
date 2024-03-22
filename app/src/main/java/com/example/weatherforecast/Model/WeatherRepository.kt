package com.example.weatherforecast.Model

import android.content.Context
import com.example.weatherforecast.Model.Local.Fav.FavLocalDataSource
import com.example.weatherforecast.Model.Local.Fav.FavWeather
import com.example.weatherforecast.Model.Local.Fav.InterFavLocalDataSource
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Local.Home.InterHomeLocalDataSource
import com.example.weatherforecast.Model.Local.Home.HomeLocalDataSource
import com.example.weatherforecast.Model.Remote.InterWeatherRemoteDataSource
import com.example.weatherforecast.Model.Remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.StateFlow

object WeatherRepository : InterWeatherRepository {

    private var remoteWeather: InterWeatherRemoteDataSource = WeatherRemoteDataSource()
    private var roomHomeWeather: InterHomeLocalDataSource = HomeLocalDataSource()
    private var roomFavWeather: InterFavLocalDataSource = FavLocalDataSource()

    override suspend fun getAdditionalWeatherRemoteRepo(
        lat: Double, lon: Double, key: String, units: String, lang: String,cnt: Int) =
        remoteWeather.getAdditionalWeatherRemote(lat,lon,key,units,lang,cnt)



    override suspend fun getAllHomeWeatherLocalRepo(context: Context): StateFlow<List<HomeWeather>>{
        return roomHomeWeather.getAllHomeWeatherLocal(context)
    }

    override suspend fun deleteAllHomeWeatherLocalRepo(context: Context): Int{
        return roomHomeWeather.deleteAllHomeWeatherLocal(context)
    }

    override suspend fun insertAllHomeWeatherLocalRepo(homeWeather: HomeWeather, context: Context): Long{
        return roomHomeWeather.insertAllHomeWeatherLocal(homeWeather,context)
    }

    override suspend fun getFavWeatherLocalRepo(context: Context): StateFlow<List<FavWeather>> {
        return roomFavWeather.getFavWeatherLocal(context)
    }

    override suspend fun deleteFavWeatherLocalRepo(favWeather: FavWeather, context: Context): Int {
        return roomFavWeather.deleteFavWeatherLocal(favWeather,context)
    }

    override suspend fun deleteAllFavWeatherLocalRepo(context: Context): Int {
        return roomFavWeather.deleteAllFavWeatherLocal(context)
    }

    override suspend fun insertFavWeatherLocalRepo(favWeather: FavWeather, context: Context): Long {
        return roomFavWeather.insertFavWeatherLocal(favWeather,context)
    }


}