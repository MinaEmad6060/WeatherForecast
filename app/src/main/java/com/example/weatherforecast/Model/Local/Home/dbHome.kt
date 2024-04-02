package com.example.weatherforecast.Model.Local.Home

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(HomeWeather::class), version = 3 )
abstract class dbHome : RoomDatabase() {
    abstract fun getHomeWeatherDao(): HomeWeatherDAO
    companion object{
        @Volatile
        private var INSTANCE: dbHome? = null
        fun getInstance (ctx: Context): dbHome {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, dbHome::class.java, "HomeWeatherDB")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance }
        }
    }
}