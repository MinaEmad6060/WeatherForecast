package com.example.weatherforecast.Model.Remote.Alert

data class OneCallAlert (
    val timezone: String="none",
    val alerts: MutableList<AlertDetails> = mutableListOf(AlertDetails())
)

data class AlertDetails (
    val event: String="No detected alerts",
    val description: String="Weather is fine!"
)