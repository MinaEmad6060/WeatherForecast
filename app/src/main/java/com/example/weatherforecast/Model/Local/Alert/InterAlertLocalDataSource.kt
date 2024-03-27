package com.example.weatherforecast.Model.Local.Alert

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

interface InterAlertLocalDataSource {

    fun getRoomInstance(context: Context): AlertWeatherDAO
    fun getAlertWeatherLocal(context: Context): StateFlow<List<AlertCalendar>>
//    suspend fun deleteAlertWeatherLocal(id: Int, context: Context): Int
    suspend fun deleteAlertWeatherLocal(id: String, context: Context): Int
    suspend fun insertAlertWeatherLocal(alertCalendar: AlertCalendar, context: Context): Long
    suspend fun deleteAllAlertWeatherLocal(context: Context): Int
}