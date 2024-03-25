package com.example.weatherforecast.Model.Local.Alert

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertWeatherDAO {
    @Query("SELECT * FROM alert_table")
    fun getAlertWeather(): Flow<List<AlertCalendar>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alertCalendar: AlertCalendar): Long

//    @Query("DELETE FROM alert_table WHERE id = :id")
//    suspend fun delete(id: Int): Int
    @Query("DELETE FROM alert_table WHERE infoOfAlert = :id")
    suspend fun delete(id: String): Int

    @Query("DELETE FROM alert_table")
    suspend fun deleteAll(): Int
}