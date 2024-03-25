package com.example.weatherforecast.Home.View

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecast.Model.Remote.Home.AdditionalWeather


class WeatherDiffUtil : DiffUtil.ItemCallback<AdditionalWeather>() {
    override fun areItemsTheSame(oldItem: AdditionalWeather, newItem: AdditionalWeather): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: AdditionalWeather, newItem: AdditionalWeather): Boolean {
        return oldItem == newItem
    }
}