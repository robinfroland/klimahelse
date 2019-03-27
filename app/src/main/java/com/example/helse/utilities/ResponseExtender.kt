package com.example.helse.utilities

import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

fun Response.parseLocationResponse(): MutableList<Location> {
    val body = JSONArray(this.body()?.string())
    val parsedResponse = ArrayList<Location>()

    for (i in 0 until body.length()) {
        val locationObject = body.getJSONObject(i)
        val location = locationObject.getString("name")
        val superlocation = locationObject.getJSONObject("kommune").getString("name")
        val longitude = locationObject.getString("longitude").toDouble()
        val latitude = locationObject.getString("latitude").toDouble()
        val station = locationObject.getString("eoi")
        parsedResponse.add(
            Location(
                location,
                superlocation,
                longitude,
                latitude,
                station
            )
        )
    }
    return parsedResponse
}

fun Response.parseAirqualityResponse(location: Location): AirqualityForecast {
    val data = JSONObject(this.body()?.string()).getJSONObject("data").getJSONArray("time")

    //TODO: implement userdefined timeframe for forecast. Index 0 temporarily
    val jsonObj = data.getJSONObject(0)
    val to = jsonObj.getString("to")
    val from = jsonObj.getString("from")

    val variables = jsonObj.getJSONObject("variables")
    val o3Concentration = variables.getJSONObject("o3_concentration").getDouble("value")
    val pm10Concentration = variables.getJSONObject("pm10_concentration").getDouble("value")
    val pm25Concentration = variables.getJSONObject("pm25_concentration").getDouble("value")
    val no2Concentration = variables.getJSONObject("no2_concentration").getDouble("value")
    val id = 0
    val stationID = location.stationID

    return AirqualityForecast(
        id,
        stationID,
        from,
        to,
        o3Concentration,
        pm10Concentration,
        pm25Concentration,
        no2Concentration

    )
}
