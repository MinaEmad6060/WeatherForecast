package com.example.weatherforecast.Model.Local

import android.content.Context
import com.example.weatherforecast.Model.Local.Home.HomeWeather
import com.example.weatherforecast.Model.Local.Home.HomeWeatherDAO
import com.example.weatherforecast.Model.Local.Home.dataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherLocalDataSource: InterWeatherLocalDataSource{
    override fun getAllHomeWeatherLocal(context: Context): StateFlow<List<HomeWeather>> {
        val homeWeather: List<HomeWeather> = mutableListOf()
        val stateFlow = MutableStateFlow(homeWeather)
        CoroutineScope(Dispatchers.Default).launch {
            getRoomInstance(context).getAllHomeWeather().collect {
                stateFlow.value=it
            }}
        return stateFlow
    }

    override suspend fun deleteAllHomeWeatherLocal(context: Context): Int {
        return getRoomInstance(context).delete()
    }


    override suspend fun insertAllHomeWeatherLocal(homeWeather: HomeWeather, context: Context): Long {
        return getRoomInstance(context).insert(homeWeather)
    }


    private var roomRef: HomeWeatherDAO? = null
    @Synchronized
    override fun getRoomInstance(context: Context): HomeWeatherDAO {
        if (roomRef == null) {
            val database = dataBase.getInstance(context)
            roomRef = database.getHomeWeatherDao()
        }
        return roomRef!!
    }
}