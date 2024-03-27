package com.example.weatherforecast.Model.Local.Alert

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertCalendar(
    @PrimaryKey
    var infoOfAlert: String=""
)