package com.example.weatherforecast.Model.ViewModel.Alert

import com.example.weatherforecast.Model.Remote.Alert.InterAlertRemoteDataSource
import com.example.weatherforecast.Model.Remote.Alert.OneCallAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAlertRemoteDataSource(
    private val alertOneCall: OneCallAlert
) :InterAlertRemoteDataSource{
    override suspend fun getAlertWeatherRemote(
        lat: Double,
        lon: Double,
        key: String
    ): Flow<OneCallAlert> {
        return flow {
            emit(alertOneCall)
        }
    }
}