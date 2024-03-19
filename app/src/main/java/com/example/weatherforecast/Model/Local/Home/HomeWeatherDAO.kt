package com.example.weatherforecast.Model.Local.Home

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface HomeWeatherDAO {
    @Query("SELECT * FROM weather_table")
    fun getAllHomeWeather(): Flow<List<HomeWeather>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(homeWeather: HomeWeather): Long
    @Query("DELETE FROM weather_table")
    suspend fun delete(): Int
}