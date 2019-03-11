package com.example.helse.data

data class Airquality(val from: String, val to: String, val variables: AirqualityVariables)

data class AirqualityForecast(val location: AirqualityLocation, val Airquality: Airquality)

data class AirqualityLocation(val location: String?, val superlocation: String?, val station: String?)

data class AirqualityVariables(
    val o3_concentration: Double,
    val pm10_concentration: Double,
    val pm25_concentration: Double,
    val no2_concentration: Double
)
