package com.example.weatherforecast.Model.Local.Alert

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(AlertCalendar::class), version = 5 )
abstract class dbAlert : RoomDatabase() {
    abstract fun getAlertWeatherDao(): AlertWeatherDAO
    companion object{
        @Volatile
        private var INSTANCE: dbAlert? = null
        fun getInstance (ctx: Context): dbAlert {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, dbAlert::class.java, "AlertWeatherDB")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance }
        }
    }
}