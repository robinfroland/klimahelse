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
    val latitude: Double,
    val longitude: Double,
    // unique ID per API/StationID? Add as system expand
    @PrimaryKey
    val stationID: String
) : Parcelable

val emptyLocation = Location("", "", 0.00, 0.00, "")