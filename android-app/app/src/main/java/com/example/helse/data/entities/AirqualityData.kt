package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airqualityForecast")
data class AirqualityForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val stationID: String,
    val from: String,
    val to: String,
    val riskValue: String,
    val o3_concentration: Double,
    val o3_riskValue: String,
    val pm10_concentration: Double,
    val pm10_riskValue: String,
    val pm25_concentration: Double,
    val pm25_riskValue: String,
    val no2_concentration: Double,
    val no2_riskValue: String
)

val emptyAirqualityForecast = AirqualityForecast(
    stationID = "",
    from = "",
    to = "",
    riskValue = "",
    o3_concentration = 0.00,
    o3_riskValue = "",
    pm10_concentration = 0.00,
    pm10_riskValue = "",
    pm25_concentration = 0.00,
    pm25_riskValue = "",
    no2_concentration = 0.00,
    no2_riskValue = ""
)
