package com.example.weatherforecast.Model.Repo.Alert

import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import kotlinx.coroutines.flow.Flow

interface InterAlertRepo {
    suspend fun getAlertWeatherLocalRepo(): Flow<List<AlertCalendar>>
    suspend fun deleteAlertWeatherLocalRepo(id: String): Int
    suspend fun insertAlertWeatherLocalRepo(alertCalendar: AlertCalendar): Long
}