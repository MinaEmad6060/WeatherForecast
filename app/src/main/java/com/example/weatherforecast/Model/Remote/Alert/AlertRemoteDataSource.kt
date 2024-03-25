package com.example.weatherforecast.Model.Remote.Alert

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AlertRemoteDataSource: InterAlertRemoteDataSource {
    override suspend fun getAlertWeatherRemote(
        lat: Double, lon: Double, key: String): StateFlow<OneCallAlert> {
        val stateFlow= MutableStateFlow(OneCallAlert())
        stateFlow.emit(API.retrofitAlertService.getAlertWeatherAPI(lat,lon,key))
        return stateFlow
    }
}