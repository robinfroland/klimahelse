package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "humidityForecast")
data class HumidityForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val riskValue: String,
    val latitude: Double,
    val longitude: Double,
    val humidityValue: Double,
    val temperature: Double
)

val emptyHumidityForecast = HumidityForecast(
    riskValue = "",
    latitude = 0.00,
    longitude = 0.00,
    humidityValue = 0.00,
    temperature = 0.00
)