package com.example.helse.data.entities

import android.graphics.drawable.Drawable

data class RiskCircles(
    val hourOfDay : Int,
    val overallRiskValue: String,
    val o3_concentration: String,
    val no2_concentration: String,
    val pm10_concentration: String,
    val pm25_concentration: String,
    val gaugeImg: Drawable


)