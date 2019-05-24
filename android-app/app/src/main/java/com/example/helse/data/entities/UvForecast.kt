package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.helse.utilities.RISK_NOT_AVAILABLE

@Entity(tableName = "uvForecast")
data class UvForecast(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val latitude: Double,
    val longitude: Double,
    val uvClear: Double,
    val uvPartlyCloudy: Double,
    val uvCloudy: Double,
    val uvForecast: Double,
    val riskValue: String
)

val emptyUvForecast = UvForecast(
    latitude = 0.00,
    longitude = 0.00,
    uvClear = 0.00,
    uvPartlyCloudy = 0.00,
    uvCloudy = 0.00,
    uvForecast = 0.00,
    riskValue = RISK_NOT_AVAILABLE
)