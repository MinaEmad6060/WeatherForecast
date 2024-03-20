package com.example.weatherforecast.Model.Local.Fav

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_table")
data class FavWeather(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    var lat: Double=0.0,
    var lon: Double=0.0,
    var cityName: String="",
    var temperature: Double=0.0,
    var img :String ="",
    var units: String=""
)
