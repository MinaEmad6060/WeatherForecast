package com.example.weatherforecast.Home.View

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecast.Model.CurrentWeather


class WeatherDiffUtil : DiffUtil.ItemCallback<CurrentWeather>() {
    override fun areItemsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
//        return oldItem.list[0].dt_txt == newItem.list[0].dt_txt
        return true
    }

    override fun areContentsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem == newItem
    }
}