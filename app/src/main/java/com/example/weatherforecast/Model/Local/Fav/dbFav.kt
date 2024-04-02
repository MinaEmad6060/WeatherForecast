package com.example.weatherforecast.Model.Local.Fav

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(FavWeather::class), version = 2 )
abstract class dbFav : RoomDatabase() {
    abstract fun getFavWeatherDao(): FavWeatherDAO
    companion object{
        @Volatile
        private var INSTANCE: dbFav? = null
        fun getInstance (ctx: Context): dbFav {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, dbFav::class.java, "FavWeatherDB")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance }
        }
    }
}