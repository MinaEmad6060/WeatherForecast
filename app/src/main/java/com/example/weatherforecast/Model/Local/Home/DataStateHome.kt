package com.example.weatherforecast.Model.Local.Home


sealed class DataStateHome {
    data class Success(val data: List<HomeWeather>): DataStateHome()
    class Failure(val msg: Throwable): DataStateHome()
    data object Loading : DataStateHome()
}