package com.example.weatherforecast.Model.Remote.Alert

import com.example.weatherforecast.model.Remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlertRemoteDataSource(private val alertAPI: ApiService): InterAlertRemoteDataSource {
    override suspend fun getAlertWeatherRemote(
        lat: Double, lon: Double, key: String): Flow<OneCallAlert> {
        return flow {
            emit(alertAPI.getAlertWeatherAPI(lat, lon, key))
        }
    }
}