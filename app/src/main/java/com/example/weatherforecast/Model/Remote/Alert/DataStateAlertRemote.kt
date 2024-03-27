package com.example.weatherforecast.Model.Remote.Alert


sealed class DataStateAlertRemote {
    data class Success(val data: OneCallAlert): DataStateAlertRemote()
    data class Failure(val msg: Throwable): DataStateAlertRemote()
    data object Loading: DataStateAlertRemote()
}