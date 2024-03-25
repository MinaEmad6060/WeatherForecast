package com.example.weatherforecast.Model.Local.Alert

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertCalendar(
//    @PrimaryKey(autoGenerate = true)
//    var id: Int=0,
//    var startDate: String="",
//    var endDate: String="",
//    var startTime: String="",
//    var endTime: String=""
    @PrimaryKey
    var infoOfAlert: String=""
)