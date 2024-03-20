package com.example.weatherforecast.Model.Local.Fav


sealed class DataStateFavRoom {
    data class Success(val data: List<FavWeather>): DataStateFavRoom()
    class Failure(val msg: Throwable): DataStateFavRoom()
    data object Loading : DataStateFavRoom()
}