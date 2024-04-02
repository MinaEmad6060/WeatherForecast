package com.example.weatherforecast.Model.Local.Home

import kotlinx.coroutines.flow.Flow

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