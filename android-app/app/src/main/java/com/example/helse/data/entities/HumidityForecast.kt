package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.helse.utilities.RISK_NOT_AVAILABLE

@Entity(tableName = "humidityForecast")
data class HumidityForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val from: String,
    val riskValue: String,
    val latitude: Double,
    val longitude: Double,
    val humidityValue: Double,
    val temperature: Double
)

val emptyHumidityForecast = HumidityForecast(
    from = "1990-01-01",
    riskValue = RISK_NOT_AVAILABLE,
    latitude = 0.00,
    longitude = 0.00,
    humidityValue = 0.00,
    temperature = 0.00
)