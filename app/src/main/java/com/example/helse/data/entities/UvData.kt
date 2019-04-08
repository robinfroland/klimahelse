package com.example.helse.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airqualityForecast")
data class UvForecast(
    @PrimaryKey(autoGenerate = false)
    val from: String
)
