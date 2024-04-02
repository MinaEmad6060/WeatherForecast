package com.example.weatherforecast.Model.Local.Alert

sealed class DataStateAlertRoom {
    data class Success(val data: List<AlertCalendar>): DataStateAlertRoom()
    class Failure(val msg: Throwable): DataStateAlertRoom()
    data object Loading : DataStateAlertRoom()
}