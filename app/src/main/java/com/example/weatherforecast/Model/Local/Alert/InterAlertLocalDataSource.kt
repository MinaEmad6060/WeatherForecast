package com.example.weatherforecast.Model.Local.Alert

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface InterAlertLocalDataSource {

//    fun getRoomInstance(context: Context): AlertWeatherDAO
    fun getAlertWeatherLocal(): Flow<List<AlertCalendar>>
//    suspend fun deleteAlertWeatherLocal(id: Int, context: Context): Int
    suspend fun deleteAlertWeatherLocal(id: String): Int
    suspend fun insertAlertWeatherLocal(alertCalendar: AlertCalendar): Long
//    suspend fun deleteAllAlertWeatherLocal(): Int
}