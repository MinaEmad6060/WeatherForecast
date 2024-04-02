package com.example.weatherforecast.Model.Local.Home

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class HomeWeather(
    @PrimaryKey
    var id: Int=0,
    var date: String="",
    var time: String="",
    var cityName: String="",
    var weatherDescription: String="",
    var weatherIcon: String="",
    var temperature: String="",
    var minTemperature: String="",
    var maxTemperature: String="",
    var humidity: String="",
    var pressure: String="",
    var windSpeed: String="",
    var clouds: String="",
    var units: String=""
)