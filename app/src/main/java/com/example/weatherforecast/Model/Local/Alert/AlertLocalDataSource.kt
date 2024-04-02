package com.example.weatherforecast.Model.Local.Alert

import kotlinx.coroutines.flow.Flow

class AlertLocalDataSource(private val dao: AlertWeatherDAO): InterAlertLocalDataSource {

    override fun getAlertWeatherLocal(): Flow<List<AlertCalendar>> {
        return dao.getAlertWeather()
    }

    override suspend fun deleteAlertWeatherLocal(id: String): Int {
        return dao.delete(id)
    }

    override suspend fun insertAlertWeatherLocal(alertCalendar: AlertCalendar): Long {
        return dao.insert(alertCalendar)
    }

}