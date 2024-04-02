package com.example.weatherforecast.Model.Local.Alert

import kotlinx.coroutines.flow.Flow

interface InterAlertLocalDataSource {

    fun getAlertWeatherLocal(): Flow<List<AlertCalendar>>
    suspend fun deleteAlertWeatherLocal(id: String): Int
    suspend fun insertAlertWeatherLocal(alertCalendar: AlertCalendar): Long

}