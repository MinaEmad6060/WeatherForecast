package com.example.weatherforecast.Model

data class CurrentWeather(
    var city: City= City(""),
    var time: String= "",
    var date: String= "",
    var list: MutableList<AdditionalWeather> = mutableListOf(AdditionalWeather())
)

data class Weather(val description:String, val icon: String)

data class Main(val temp: Double,
                val temp_min:Double,
                val temp_max:Double,
                val humidity: String,
                val pressure: String)

data class Wind(val speed: Double)

data class City(val name: String)

data class Clouds(val all: Int)
data class AdditionalWeather(
    var weather: MutableList<Weather> = mutableListOf(Weather("", "")),
    var main: Main= Main(0.0, 0.0, 0.0, "", ""),
    var wind: Wind= Wind(0.0),
    var clouds: Clouds= Clouds(0),
    var dt_txt:String="")