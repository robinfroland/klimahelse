package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "humidityForecast")
data class HumidityForecast(
    @PrimaryKey(autoGenerate = false)
    val riskValue: String,
    val latitude: Double,
    val longitude: Double,
    val humidityValue: Double,
    val temperature: Double
)

val emptyHumidityForecast = HumidityForecast(
    "",
    0.00,
    0.00,
    0.00,
    0.00
)