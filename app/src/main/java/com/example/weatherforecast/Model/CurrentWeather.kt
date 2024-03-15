package com.example.weatherforecast.Model

data class CurrentWeather(
    val weather: MutableList<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val name: String,
    var time: String,
    var date: String
)

data class Weather(val description:String, val icon: String)

data class Main(val temp: Double, val humidity: String, val pressure: String)

data class Wind(val speed: Double)

data class Clouds(val all: Int)