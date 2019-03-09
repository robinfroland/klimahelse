package com.example.helse.api.forecast

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

interface AirqualityForecastApi {
    fun fetchAirquality(): List<AirqualityForecast>
}

class AirqualityForecastApiImpl(private val station: String) : AirqualityForecastApi {

    private val client = OkHttpClient()
    private val baseUrl = "https://api.met.no/weatherapi/airqualityforecast/0.1/?station="

    override fun fetchAirquality(): List<AirqualityForecast> {
        try {
            val request = Request.Builder()
                .url(baseUrl.plus(station.trim()))
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                print("responseCode: ${response.code()}")
                throw Error("Something went wrong, error code is not 200 ${response.message()}")
            }

            return parseAirqualityForecastData(response)
        } catch (e: Exception) {
            Log.getStackTraceString(e)
        }
        return emptyList()
    }
}

fun parseAirqualityForecastData(response: Response): ArrayList<AirqualityForecast> {
    val bodyAsJSON = JSONObject(response.body()?.string())
    val parsedData = ArrayList<AirqualityForecast>()

    val data = bodyAsJSON.getJSONObject("data").getJSONArray("time")
    val meta = bodyAsJSON.getJSONObject("meta")

    val kommune = meta.getJSONObject("superlocation").getString("name")
    val name = meta.getJSONObject("location").getString("name")
    val station = meta.getJSONObject("location").getString("areacode")

    for (i in 0 until data.length()) {
        val jsonObj = data.getJSONObject(i)
        val to = jsonObj.getString("to")
        val from = jsonObj.getString("from")

        val variables = jsonObj.getJSONObject("variables")
        val o3Concentration = variables.getJSONObject("o3_concentration").getDouble("value")
        val pm10Concentration = variables.getJSONObject("pm10_concentration").getDouble("value")
        val pm25Concentration = variables.getJSONObject("pm25_concentration").getDouble("value")
        val no2Concentration = variables.getJSONObject("no2_concentration").getDouble("value")

        parsedData.add(
            AirqualityForecast(
                AirqualityLocation(kommune, name, station),
                Airquality(
                    from,
                    to,
                    AirqualityVariables(
                        o3Concentration,
                        pm10Concentration,
                        pm25Concentration,
                        no2Concentration
                    )
                )
            )
        )
    }
    return parsedData
}