package com.example.weatherforecast.Favourite.View

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecast.Model.CurrentWeather

class FavWeatherDiffUtil : DiffUtil.ItemCallback<CurrentWeather>() {
    override fun areItemsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem == newItem
    }
}