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
    val riskValue = variables.getJSONObject("AQI").getDouble("value")
    val stationID = location.stationID
    val o3RiskValue = checkRiskValue(o3Calculate(o3Concentration))
    val pm10RiskValue = checkRiskValue(pm10Calculate(pm10Concentration))
    val pm25RiskValue = checkRiskValue(pm25Calculate(pm25Concentration))
    val no2RiskValue = checkRiskValue(no2Calculate(no2Concentration))
    val riskString: String = checkRiskValue(riskValue)


    return AirqualityForecast(
        stationID,
        from,
        to,
        riskString,
        o3Concentration,
        o3RiskValue,
        pm10Concentration,
        pm10RiskValue,
        pm25Concentration,
        pm25RiskValue,
        no2Concentration,
        no2RiskValue

    )
}

fun checkRiskValue(aqi : Double): String {
    if(aqi <= LOW_AQI_VALUE) return "LAV"
    if(aqi <= MEDIUM_AQI_VALUE) return "MODERAT"
    if(aqi <= HIGH_AQI_VALUE) return "BETYDELIG"
    if(aqi <= VERY_HIGH_AQI_VALUE) return "ALVORLIG"
    else return "N/A"
}

//NB! This might not be needed

//these methods calculates
fun pm25Calculate (x : Double): Double {
    var aqi: Double
    if (x < 0.999) aqi = 1.00
    else if ( x < 30.0 ) aqi = (x / 30.0) + 1.00
    else if ( x < 50.0 ) aqi = ((x - 30.0) / (50.0 - 30.0)) + 2.00
    else if ( x < 150.0 ) aqi = ((x - 50.0) / (150.0 - 50.0)) + 3.00
    else aqi = (x / 150.0) + 3.00
    if ( aqi > 4.999 ) aqi = 4.999
    return aqi
}
fun no2Calculate (x : Double): Double {
    var aqi: Double
    if ( x < 0.00 ) aqi = 1.00
    else if ( x < 100.0 ) aqi = (x / 100.0) + 1.00
    else if ( x < 200.0 ) aqi = ((x - 100.0) / (200.0 - 100.0)) + 2.00
    else if ( x < 400.0 ) aqi = ((x - 200.0) / (400.0 - 200.0)) + 3.00
    else aqi = (x / 400.0) + 3.00
    if ( aqi > 4.999) aqi = 4.999
    return aqi
}
fun pm10Calculate (x : Double): Double {
    var aqi: Double

    if ( x < 0.00 ) aqi = 1.00
    else if ( x < 60.0 ) aqi = (x / 60.0) + 1.00
    else if ( x < 120.0 ) aqi = ((x - 60.0) / (120.0 - 60.0)) + 2.00
    else if ( x < 400.0 ) aqi = ((x - 120.0) / (400.0 - 120.0)) + 3.00
    else aqi = (x / 400.0) + 3.00
    if ( aqi > 4.999 ) aqi = 4.999
    return aqi
}
fun o3Calculate (x : Double): Double {
    var aqi: Double

    if ( x < 0.00 ) aqi = 1.00
    else if ( x < 100.0 ) aqi = (x / 100.0) + 1.00
    else if ( x < 180.0 ) aqi = ((x - 100.0) / (180.0 - 100.0)) + 2.00
    else if ( x < 240.0 ) aqi = ((x - 180.0) / (240.0 - 180.0)) + 3.00
    else aqi = (x / 240.0) + 3.00
    if ( aqi > 4.999 ) aqi = 4.999
    return aqi

}
