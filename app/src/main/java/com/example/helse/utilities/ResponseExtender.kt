package com.example.helse.utilities

import android.util.Xml
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser

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

fun Response.parseAirqualityResponse(location: Location): MutableList<AirqualityForecast> {
    val data = JSONObject(this.body()?.string()).getJSONObject("data").getJSONArray("time")
    val aqiForecastTimeArray = ArrayList<AirqualityForecast>()

    for (i in 0..(data.length() - 1)) {
        val jsonObj = data.getJSONObject(i)
        val to = jsonObj.getString("to")
        val from = jsonObj.getString("from")

        val variables = jsonObj.getJSONObject("variables")
        val o3Concentration = variables.getJSONObject("o3_concentration").getDouble("value")
        val pm10Concentration = variables.getJSONObject("pm10_concentration").getDouble("value")
        val pm25Concentration = variables.getJSONObject("pm25_concentration").getDouble("value")
        val no2Concentration = variables.getJSONObject("no2_concentration").getDouble("value")
        val stationID = location.stationID
        val o3RiskValue = calculateRiskFor(AirqualityMetrics.PM25, o3Concentration)
        val pm10RiskValue = calculateRiskFor(AirqualityMetrics.PM10, pm10Concentration)
        val pm25RiskValue = calculateRiskFor(AirqualityMetrics.PM25, pm25Concentration)
        val no2RiskValue = calculateRiskFor(AirqualityMetrics.NO2, no2Concentration)
        val overallRisk = calculateOverallRiskValue(arrayOf(o3RiskValue, pm10RiskValue, pm25RiskValue, no2RiskValue))

        val tempAqi = AirqualityForecast(
            stationID,
            from,
            to,
            overallRisk,
            o3Concentration,
            o3RiskValue,
            pm10Concentration,
            pm10RiskValue,
            pm25Concentration,
            pm25RiskValue,
            no2Concentration,
            no2RiskValue
        )
        aqiForecastTimeArray.add(tempAqi)
    }

    return aqiForecastTimeArray
}

fun Response.parseUvResponse(currentLocation: Location): MutableList<UvForecast> {
    val parsedResponse = ArrayList<UvForecast>()

    this.body()?.byteStream()
        .use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
                .apply { setInput(inputStream, null) }
            var previousDistance = Double.MAX_VALUE

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                if (parser.name == "location") {
                    val latitude = parser.getAttributeValue(0).toDouble()
                    val longitude = parser.getAttributeValue(1).toDouble()
                    repeat(4) {
                        parser.next()
                    }
                    val uviClear = parser.getAttributeValue(1).toDouble()
                    repeat(3) {
                        parser.next()
                    }
                    val uviPartlyCloudy = parser.getAttributeValue(1).toDouble()
                    repeat(3) {
                        parser.next()
                    }
                    val uviCloudy = parser.getAttributeValue(1).toDouble()
                    repeat(3) {
                        parser.next()
                    }
                    val uviForecast = parser.getAttributeValue(1).toDouble()

                    val distance = calculateDistanceBetweenCoordinates(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        latitude,
                        longitude
                    )

                    if (distance < previousDistance) {
                        parsedResponse.add(
                            0,
                            UvForecast(
                                latitude = latitude,
                                longitude = longitude,
                                uvClear = uviClear,
                                uvPartlyCloudy = uviPartlyCloudy,
                                uvCloudy = uviCloudy,
                                uvForecast = uviForecast,
                                riskValue = calculateUvRiskValue(uviClear)
                            )
                        )
                        previousDistance = distance
                    }
                }
            }
        }
    return parsedResponse
}
