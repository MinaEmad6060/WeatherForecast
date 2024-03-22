package com.example.weatherforecast.Model.Remote

sealed class DataStateRemote {
    data class Success(val data: CurrentWeather): DataStateRemote()
    data class Failure(val msg: Throwable): DataStateRemote()
    data object Loading: DataStateRemote()
}