package com.example.weatherforecast.Model

data class CurrentWeather(
    var city: City= City(""),
    var time: String= "",
    var date: String= "",
    var list: MutableList<AdditionalWeather> = mutableListOf(AdditionalWeather())
)

data class Weather(var description:String, val icon: String)

data class Main(
    var temp: Double,
    val temp_min:Double,
    val temp_max:Double,
    var humidity: String,
    var pressure: String)

data class Wind(var speed: Double)

data class City(val name: String)

data class Clouds(var all: Int)
data class AdditionalWeather(
    var weather: MutableList<Weather> = mutableListOf(Weather("", "")),
    var main: Main= Main(0.0, 0.0, 0.0, "", ""),
    var wind: Wind= Wind(0.0),
    var clouds: Clouds= Clouds(0),
    var dt_txt:String="")