package com.example.weatherforecast.Model.ViewModel.Alert

import com.example.weatherforecast.Model.Local.Alert.AlertCalendar
import com.example.weatherforecast.Model.Local.Alert.InterAlertLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FakeAlertLocalDataSource(
    private var alertList: MutableList<AlertCalendar> = mutableListOf()
) :InterAlertLocalDataSource{
    override fun getAlertWeatherLocal(): Flow<List<AlertCalendar>> =
        flow {
            emit(alertList.toList())
        }

    override suspend fun deleteAlertWeatherLocal(id: String): Int {
        val removed = alertList.removeAll { it.infoOfAlert == id }
        return if (removed) 1 else 0
    }

    override suspend fun insertAlertWeatherLocal(alertCalendar: AlertCalendar): Long {
        return if(alertList.add(alertCalendar)) 1 else 0
    }

}