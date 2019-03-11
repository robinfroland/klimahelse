package com.example.helse.data.entity

import androidx.room.Entity


@Entity
data class Airquality(val from: String, val to: String, val variables: AirqualityVariables)

data class AirqualityForecast(val location: AirqualityLocation, val Airquality: Airquality)

data class AirqualityVariables(
    val o3_concentration: Double,
    val pm10_concentration: Double,
    val pm25_concentration: Double,
    val no2_concentration: Double
)
