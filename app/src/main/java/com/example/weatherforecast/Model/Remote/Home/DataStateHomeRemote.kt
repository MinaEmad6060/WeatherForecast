package com.example.weatherforecast.Model.Remote.Home

sealed class DataStateHomeRemote {
    data class Success(val data: CurrentWeather): DataStateHomeRemote()
    data class Failure(val msg: Throwable): DataStateHomeRemote()
    data object Loading: DataStateHomeRemote()
}