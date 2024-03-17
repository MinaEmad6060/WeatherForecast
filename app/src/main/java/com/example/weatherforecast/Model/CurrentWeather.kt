package com.example.weatherforecast.Model

data class CurrentWeather(
    var weather: MutableList<Weather> = mutableListOf(Weather("Sunny", "01d")),
    var main: Main= Main(25.0, 20.0, 30.0, "50", "1013"),
    var wind: Wind= Wind(5.0),
    var clouds: Clouds= Clouds(20),
    var name: String= "London",
    var time: String= "12:00 PM",
    var date: String= "2024-03-17",
    var list: MutableList<AdditionalWeather> = mutableListOf(AdditionalWeather())
)

data class Weather(val description:String, val icon: String)

data class Main(val temp: Double,
                val temp_min:Double,
                val temp_max:Double,
                val humidity: String,
                val pressure: String)

data class Wind(val speed: Double)

data class Clouds(val all: Int)
data class AdditionalWeather(
    var weather: MutableList<Weather> = mutableListOf(Weather("Sunny", "01d")),
    var main: Main= Main(25.0, 20.0, 30.0, "50", "1013"),
    var dt_txt:String="2024-03-17 12:00:00")