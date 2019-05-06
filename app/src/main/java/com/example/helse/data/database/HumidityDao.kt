package com.example.helse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.helse.data.entities.HumidityForecast

@Dao
interface HumidityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(humdity: MutableList<HumidityForecast>)

    @Query("DELETE FROM humidityForecast")
    fun deleteAll()

    @Query("SELECT * FROM humidityForecast")
    fun getAll(): MutableList<HumidityForecast>
}