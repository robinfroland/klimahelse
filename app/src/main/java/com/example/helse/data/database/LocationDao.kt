package com.example.helse.data.database

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.helse.data.entity.Location

interface LocationDao {
    @Query("SELECT * FROM airquality_locations ORDER BY superlocation")
    fun getAllLocations(): LiveData<List<Location>>

    // Get by what attribute?
    @Query("SELECT * FROM airquality_locations WHERE location = :location")
    fun getLocation(location: String): LiveData<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: List<Location>)
}