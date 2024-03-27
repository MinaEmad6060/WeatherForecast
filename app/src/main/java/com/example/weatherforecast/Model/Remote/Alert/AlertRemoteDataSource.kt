package com.example.weatherforecast.Model.Remote.Alert

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class AlertRemoteDataSource(private val alertAPI: AlertAPI): InterAlertRemoteDataSource {
    override suspend fun getAlertWeatherRemote(
        lat: Double, lon: Double, key: String): Flow<OneCallAlert> {
        return flow {
            emit(alertAPI.getAlertWeatherAPI(lat, lon, key))
        }
    }
}