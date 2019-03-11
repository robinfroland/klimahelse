package com.example.helse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Constant to retrieve currently selected location
const val CURRENT_LOCATION_ID = 0 // TODO: create key for current

@Entity(tableName = "airquality_locations")
data class Location(
    val location: String,
    val superlocation: String,
    val stationID: String,
    // unique ID per API/StationID? Add as system expand
    @PrimaryKey
    val id: String = location)