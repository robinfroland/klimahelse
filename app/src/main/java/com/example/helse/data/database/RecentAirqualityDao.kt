package com.example.helse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.helse.data.entities.AirqualityForecast

@Dao
interface RecentAirqualityDao {
    @Query("SELECT * FROM airqualityForecast")
    fun getAirquality(): AirqualityForecast

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(airquality: AirqualityForecast)
}