package com.example.helse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location

@Dao
interface AirqualityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(airquality: MutableList<AirqualityForecast>)

    @Query("DELETE FROM airqualityForecast where stationID == :stationID")
    fun delete(stationID: String)

    @Query("DELETE FROM airqualityForecast")
    fun deleteAll()

    @Query("SELECT * FROM airqualityForecast where stationID == :stationID")
    fun get(stationID: String): MutableList<AirqualityForecast>

    @Query("SELECT * FROM airqualityForecast")
    fun getAll(): MutableList<AirqualityForecast>
}