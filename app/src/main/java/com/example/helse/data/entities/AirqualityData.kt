package com.example.helse.data.entities

import androidx.room.Entity


@Entity
data class Airquality(val from: String, val to: String, val variables: AirqualityVariables)

@Entity
data class AirqualityForecast(val location: Location, val Airquality: Airquality)

@Entity
data class AirqualityVariables(
    val o3_concentration: Double,
    val pm10_concentration: Double,
    val pm25_concentration: Double,
    val no2_concentration: Double
)