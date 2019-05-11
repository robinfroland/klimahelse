package com.example.helse.data.entities


data class RiskCircles(
    val hourOfDay: Int,
    val dateAndDay: String,
    val overallRiskValue: String,
    val o3_concentration: String,
    val no2_concentration: String,
    val pm10_concentration: String,
    val pm25_concentration: String
)