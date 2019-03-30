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
    var id: Int,
    val stationID: String,
    @PrimaryKey(autoGenerate = false)
    val from: String,
    val to: String,
    val riskValue: String,
    val o3_concentration: Double,
    val o3_riskValue: String,
    val pm10_concentration: Double,
    val pm10_riskValue: String,
    val pm25_concentration: Double,
    val pm25_riskValue: String,
    val no2_concentration: Double,
    val no2_riskValue: String

)

val emptyAirqualityForecast = AirqualityForecast(
    0,
    "",
    "",
    "",
    "",
    0.00,
    "",
    0.00,
    "",
    0.00,
    "",
    0.00,
    ""
)
