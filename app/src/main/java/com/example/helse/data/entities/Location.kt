package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Constant to retrieve currently selected location
const val CURRENT_LOCATION_ID = 0 // TODO: create key for selected location

@Entity(tableName = "locations")
data class Location(
    val location: String,
    val superlocation: String,
    val longitude: Double,
    val latitude: Double,
    // unique ID per API/StationID? Add as system expand
    @PrimaryKey
    val stationID: String
)