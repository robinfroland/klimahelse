package com.example.helse.data.entities

import androidx.room.Entity


//Database needs a primarykey here unsure as to where to put it.
@Entity(tableName = "airqualityForecast")
data class AirqualityForecast(
    val location : Location,
    val from: String,
    val to: String,
    val o3_concentration: Double,
    val pm10_concentration: Double,
    val pm25_concentration: Double,
    val no2_concentration: Double
)
