package com.example.weatherforecast.Model.Local.Fav

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavWeatherDAO {
    @Query("SELECT * FROM fav_table")
    fun getFavWeather(): Flow<List<FavWeather>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favWeather: FavWeather): Long

    @Delete
    suspend fun delete(favWeather: FavWeather): Int

    @Query("DELETE FROM fav_table")
    suspend fun deleteAll(): Int
}