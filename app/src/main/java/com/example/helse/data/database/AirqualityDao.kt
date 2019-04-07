package com.example.helse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.helse.data.entities.AirqualityForecast

@Dao
interface AirqualityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(airquality: MutableList<AirqualityForecast>)

    @Query("DELETE FROM airqualityForecast")
    fun deleteAll()

    @Query("SELECT * FROM airqualityForecast")
    fun getAll(): MutableList<AirqualityForecast>
}