package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airqualityForecast")
data class UvForecast(
    @PrimaryKey(autoGenerate = false)
    val latitude: Double,
    val longitude: Double,
    val uvClear: Double,
    val uvPartlyCloudy: Double,
    val uvCloudy: Double,
    val uvForecast: Double,
    val riskValue: String
)

val emptyUvForecast = UvForecast(
    0.00,
    0.00,
    0.00,
    0.00,
    0.00,
    0.00,
    ""
)