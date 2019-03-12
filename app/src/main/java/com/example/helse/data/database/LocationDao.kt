package com.example.helse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.helse.data.entities.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations ORDER BY superlocation")
    fun getAllLocations(): List<Location>

    // Get by what attribute?
    @Query("SELECT * FROM locations WHERE location = :location OR superlocation = :location")
    fun getLocation(location: String): Location

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: List<Location>)
}