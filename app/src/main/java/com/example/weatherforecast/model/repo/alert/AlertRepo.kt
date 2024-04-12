package com.example.weatherforecast.Model.Repo.Alert


import com.example.weatherforecast.Model.Local.Alert.AlertCalendar

import com.example.weatherforecast.Model.Local.Alert.InterAlertLocalDataSource



import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class AlertRepo(

    private var roomAlertWeather: InterAlertLocalDataSource
): InterAlertRepo {


    override suspend fun insertAlertWeatherLocalRepo(alertCalendar: AlertCalendar): Long {
        return roomAlertWeather.insertAlertWeatherLocal(alertCalendar)
    }

    override suspend fun getAlertWeatherLocalRepo(): Flow<List<AlertCalendar>> {
        return roomAlertWeather.getAlertWeatherLocal()
    }
    override suspend fun deleteAlertWeatherLocalRepo(id: String): Int {
        return roomAlertWeather.deleteAlertWeatherLocal(id)
    }


}