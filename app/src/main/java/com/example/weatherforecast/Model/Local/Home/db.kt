package com.example.weatherforecast.Model.Local.Home

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(HomeWeather::class), version = 3 )
abstract class dataBase : RoomDatabase() {
    abstract fun getHomeWeatherDao(): HomeWeatherDAO
    companion object{
        @Volatile
        private var INSTANCE: dataBase? = null
        fun getInstance (ctx: Context): dataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, dataBase::class.java, "HomeWeatherDB")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance }
        }
    }
}