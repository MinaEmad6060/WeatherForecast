package com.example.weatherforecast.Model

data class CurrentWeather(val weather:MutableList<Weather>)

data class Weather(val description:String)