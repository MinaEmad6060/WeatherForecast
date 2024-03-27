package com.example.weatherforecast.Model.Local.Home

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeLocalDataSource(private val dao: HomeWeatherDAO): InterHomeLocalDataSource {
    override fun getAllHomeWeatherLocal(): Flow<List<HomeWeather>> {
        return dao.getAllHomeWeather()

    }

    override suspend fun deleteAllHomeWeatherLocal(): Int {
        return dao.delete()
    }


    override suspend fun insertAllHomeWeatherLocal(homeWeather: HomeWeather): Long {
        return dao.insert(homeWeather)
    }


}