package com.example.helse.airquality

import java.io.Serializable

data class Airquality(val from: String, val to: String, val variables: AirqualityVariables)

data class AirqualityForecast(val location: AirqualityLocation, val Airquality: Airquality)

data class AirqualityLocation(val kommune: String?, val name: String?, val station: String?)

data class AirqualityVariables(
    val o3_concentration: Double,
    val pm10_concentration: Double,
    val pm25_concentration: Double,
    val no2_concentration: Double
)
