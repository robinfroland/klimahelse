package com.example.helse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Constant to retrieve currently selected location
const val CURRENT_LOCATION_ID = 0 // TODO: create key for current

@Entity(tableName = "airquality_locations")
data class AirqualityLocation(
    val location: String,
    val superlocation: String,
    @PrimaryKey
    val stationID: String)