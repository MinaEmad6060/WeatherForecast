package com.example.weatherforecast.Model.Repo.Alert

import android.content.Context
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface InterAlertRepo {
    suspend fun getAlertWeatherLocalRepo(): Flow<List<AlertCalendar>>
    suspend fun deleteAlertWeatherLocalRepo(id: String): Int
    suspend fun insertAlertWeatherLocalRepo(alertCalendar: AlertCalendar): Long
}

