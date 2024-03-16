package com.example.weatherforecast.Model.Remote

import com.example.weatherforecast.Model.Main
import com.example.weatherforecast.Model.Weather

data class DetailedWeather(
    val test:String
//    val list: MutableList<WeatherList>,
//    val weather: MutableList<Weather>,
//    val main: Main
)

data class WeatherList(val dt_txt:String)