package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.helse.data.database.LocationConverter


//Database needs a primarykey here unsure as to where to put it.
@Entity(tableName = "airqualityForecast")
data class AirqualityForecast(
    @PrimaryKey(autoGenerate = true)
    @TypeConverters(LocationConverter::class)
    val location : Location,
    val from: String,
    val to: String,
    val o3_concentration: Double,
    val pm10_concentration: Double,
    val pm25_concentration: Double,
    val no2_concentration: Double
)

val emptyAirqualityForecast = AirqualityForecast(
    Location(
        "",
        "",
        0.00,
        0.00,
        ""
    ),

    "",
    "",
    0.00,
    0.00,
    0.00,
    0.00
)

