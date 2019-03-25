package com.example.helse.data.entities
import androidx.room.*

/*@Entity(tableName = "airqualityForecast", foreignKeys =
[ForeignKey(
    entity = Location::class,
    parentColumns = ["stationID"],
    childColumns = ["stationID"],
    onDelete = ForeignKey.CASCADE)
])*/

@Entity(tableName = "airqualityForecast")
data class AirqualityForecast(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    val stationID: String,
    val from: String,
    val to: String,
    val o3_concentration: Double,
    val pm10_concentration: Double,
    val pm25_concentration: Double,
    val no2_concentration: Double
)

val emptyAirqualityForecast = AirqualityForecast(
    0,
    "",
    "",
    "",
    0.00,
    0.00,
    0.00,
    0.00
)