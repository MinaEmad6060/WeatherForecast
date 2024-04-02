package com.example.weatherforecast.Alert.View

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecast.Model.Local.Alert.AlertCalendar

class AlertWeatherDiffUtil : DiffUtil.ItemCallback<AlertCalendar>() {

override fun areItemsTheSame(oldItem: AlertCalendar, newItem: AlertCalendar): Boolean {
    return oldItem.infoOfAlert == newItem.infoOfAlert
}

    override fun areContentsTheSame(oldItem: AlertCalendar, newItem: AlertCalendar): Boolean {
        return oldItem == newItem
    }
}