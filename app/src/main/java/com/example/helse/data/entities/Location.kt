package com.example.helse.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "locations")
data class Location(
    val location: String,
    val superlocation: String,
    val longitude: Double,
    val latitude: Double,
    // unique ID per API/StationID? Add as system expand
    @PrimaryKey
    val stationID: String
) : Parcelable