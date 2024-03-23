//package com.example.weatherforecast.Alert.View
//
//
//import androidx.recyclerview.widget.DiffUtil
//import com.example.weatherforecast.Model.Local.Fav.FavWeather
//import com.example.weatherforecast.Model.Remote.CurrentWeather
//
//class FavWeatherDiffUtil : DiffUtil.ItemCallback<FavWeather>() {
//    override fun areItemsTheSame(oldItem: FavWeather, newItem: FavWeather): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: FavWeather, newItem: FavWeather): Boolean {
//        return oldItem == newItem
//    }
//}