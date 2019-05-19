package com.example.helse.utilities

import android.util.Log
import android.util.Xml
import com.example.helse.data.entities.*
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
        val latitude = locationObject.getString("latitude").toDouble()
        val longitude = locationObject.getString("longitude").toDouble()
        val station = locationObject.getString("eoi")
        parsedResponse.add(
            Location(
                location = location,
                superlocation = superlocation,
                latitude = latitude,
                longitude = longitude,
                stationID = station
            )
        )
    }
    return parsedResponse
}

fun Response.parseAirqualityResponse(location: Location): MutableList<AirqualityForecast> {
    val data = JSONObject(this.body()?.string()).getJSONObject("data").getJSONArray("time")
    val aqiForecastTimeArray = ArrayList<AirqualityForecast>()

    for (i in 0 until 24) {
        val jsonObj = data.getJSONObject(i)
        val to = jsonObj.getString("to")
        val from = jsonObj.getString("from")

        val variables = jsonObj.getJSONObject("variables")
        val o3Concentration = variables.getJSONObject("o3_concentration").getDouble("value")
        val pm10Concentration = variables.getJSONObject("pm10_concentration").getDouble("value")
        val pm25Concentration = variables.getJSONObject("pm25_concentration").getDouble("value")
        val no2Concentration = variables.getJSONObject("no2_concentration").getDouble("value")
        val stationID = location.stationID
        val o3RiskValue = calculateRiskFor(AirqualityMetrics.O3, o3Concentration)
        val pm10RiskValue = calculateRiskFor(AirqualityMetrics.PM10, pm10Concentration)
        val pm25RiskValue = calculateRiskFor(AirqualityMetrics.PM25, pm25Concentration)
        val no2RiskValue = calculateRiskFor(AirqualityMetrics.NO2, no2Concentration)
        val overallRisk = calculateOverallRiskValue(arrayOf(o3RiskValue, pm10RiskValue, pm25RiskValue, no2RiskValue))

        val tempAqi = AirqualityForecast(
            stationID = stationID,
            from = from,
            to = to,
            o3_concentration = o3Concentration,
            riskValue = overallRisk,
            o3_riskValue = o3RiskValue,
            pm10_concentration = pm10Concentration,
            pm10_riskValue = pm10RiskValue,
            pm25_concentration = pm25Concentration,
            pm25_riskValue = pm25RiskValue,
            no2_concentration = no2Concentration,
            no2_riskValue = no2RiskValue
        )
        aqiForecastTimeArray.add(tempAqi)
    }

    return aqiForecastTimeArray
}

fun Response.parseUvResponse(currentLocation: Location): UvForecast {
    var uvForecast: UvForecast = emptyUvForecast

    try {
        this.body()?.byteStream()
            .use { inputStream ->
                val parser: XmlPullParser = Xml.newPullParser()
                    .apply { setInput(inputStream, null) }
                var previousDistance = Double.MAX_VALUE

                println("inputStream: $inputStream")

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }

                    println("parser: $parser")

                    if (parser.name == "location") {
                        println("Is location")
                        val latitude = parser.getAttributeValue(0).toDouble()
                        val longitude = parser.getAttributeValue(1).toDouble()
                        println("latitude $latitude")
                        println("longitude $longitude")
                        repeat(4) {
                            if (parser.next() == XmlPullParser.END_DOCUMENT) {
                                return uvForecast
                            }
                        }
                        println("Is location2")
                        val uviClear = parser.getAttributeValue(1).toDouble()
                        println("uviClear $uviClear")
                        repeat(3) {
                            if (parser.next() == XmlPullParser.END_DOCUMENT) {
                                return uvForecast
                            }
                        }
                        println("Is location3")
                        val uviPartlyCloudy = parser.getAttributeValue(1).toDouble()
                        println("uviPartlyCloudy $uviPartlyCloudy")
                        repeat(3) {
                            if (parser.next() == XmlPullParser.END_DOCUMENT) {
                                return uvForecast
                            }
                        }
                        println("Is location4")
                        val uviCloudy = parser.getAttributeValue(1).toDouble()
                        println("uviCloudy $uviCloudy")
                        repeat(3) {
                            if (parser.next() == XmlPullParser.END_DOCUMENT) {
                                return uvForecast
                            }
                        }
                        println("Is location5")
                        val uviForecast = parser.getAttributeValue(1).toDouble()
                        println("uviForecast $uviForecast")
                        val distance = calculateDistanceBetweenCoordinates(
                            currentLocation.latitude,
                            currentLocation.longitude,
                            latitude,
                            longitude
                        )
                        println("distance $distance")
                        println("Is location6")

                        if (distance < previousDistance) {
                            println("Less than previous")
                            uvForecast = UvForecast(
                                latitude = latitude,
                                longitude = longitude,
                                uvClear = uviClear,
                                uvPartlyCloudy = uviPartlyCloudy,
                                uvCloudy = uviCloudy,
                                uvForecast = uviForecast,
                                riskValue = calculateUvRiskValue(uviClear)
                            )

                            previousDistance = distance
                        }
                        println("Is location7")
                    }
                }
            }
    } catch (error: NumberFormatException) {
        println("App failed with error $error")
        println("error: ${error.localizedMessage}")
        return uvForecast
    }
    println("uvResponse: $uvForecast")
    return uvForecast
}

fun Response.parseHumidityResponse(currentLocation: Location): MutableList<HumidityForecast> {
    val parsedResponse = ArrayList<HumidityForecast>()
    val latitude = currentLocation.latitude
    val longitude = currentLocation.longitude

    this.body()?.byteStream()
        .use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
                .apply { setInput(inputStream, null) }

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }

                if(parsedResponse.size == 24){
                    break
                }

                if(parser.name == "time"){
                    val from = parser.getAttributeValue(1).toString()
                    repeat(2) {
                        parser.nextTag()
                    }

                    if (parser.name == "temperature") {
                        val temperature = parser.getAttributeValue(2).toDouble()
                        repeat(10) {
                            parser.nextTag()
                        }

                        if (parser.name == "humidity") {
                            val humidityValue = parser.getAttributeValue(0).toDouble()
                            val riskValue = calculateHumidityRiskValue(humidityValue)

                            parsedResponse.add(
                                HumidityForecast(
                                    from = from,
                                    riskValue = riskValue,
                                    latitude = latitude,
                                    longitude = longitude,
                                    humidityValue = humidityValue,
                                    temperature = temperature
                                )
                            )
                        }
                    }
                }
            }
        }
    return parsedResponse
}