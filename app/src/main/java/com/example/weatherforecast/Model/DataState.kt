package com.example.weatherforecast.Model

sealed class DataState {
    data class Success(val data: CurrentWeather): DataState()
    data class Failure(val msg: Throwable): DataState()
    data object Loading: DataState()
}