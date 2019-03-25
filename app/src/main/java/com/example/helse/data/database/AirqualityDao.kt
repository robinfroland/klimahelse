package com.example.helse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.helse.data.entities.AirqualityForecast

@Dao
interface AirqualityDao {
    @Query("SELECT * FROM airqualityForecast WHERE id = 0")
    fun getRecent(): AirqualityForecast

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(airquality: AirqualityForecast)

    //used to check if table is empty
    @Query("SELECT * FROM airqualityForecast")
    fun getAll(): MutableList<AirqualityForecast>
}