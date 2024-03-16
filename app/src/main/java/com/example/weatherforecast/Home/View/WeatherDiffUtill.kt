package com.example.weatherforecast.Home.View

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecast.Model.Remote.DetailedWeather


class WeatherDiffUtil : DiffUtil.ItemCallback<DetailedWeather>() {
    override fun areItemsTheSame(oldItem: DetailedWeather, newItem: DetailedWeather): Boolean {
//        return oldItem.list[0].dt_txt == newItem.list[0].dt_txt
        return true
    }

    override fun areContentsTheSame(oldItem: DetailedWeather, newItem: DetailedWeather): Boolean {
        return oldItem == newItem
    }
}