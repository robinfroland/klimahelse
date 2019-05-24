package com.example.helse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.helse.data.entities.UvForecast

@Dao
interface UVDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(uvList: List<UvForecast>)

    @Query("DELETE FROM uvForecast")
    fun deleteAll()

    @Query("SELECT * FROM uvForecast")
    fun getAll(): List<UvForecast>
}