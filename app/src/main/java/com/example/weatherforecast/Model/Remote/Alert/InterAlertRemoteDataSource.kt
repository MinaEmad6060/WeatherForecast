package com.example.weatherforecast.Model.Remote.Alert

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface InterAlertRemoteDataSource {
    suspend fun getAlertWeatherRemote(
        lat: Double, lon: Double, key: String): Flow<OneCallAlert>
}