package com.example.weatherforecast.Model.Local.Home


sealed class DataStateHomeRoom {
    data class Success(val data: List<HomeWeather>): DataStateHomeRoom()
    class Failure(val msg: Throwable): DataStateHomeRoom()
    data object Loading : DataStateHomeRoom()
}