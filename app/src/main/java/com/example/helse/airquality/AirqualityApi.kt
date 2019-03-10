package com.example.helse.airquality

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

interface AirqualityApi {
    fun fetchAirqualityLocations(): List<AirqualityLocation>
    fun fetchAirquality(): List<AirqualityForecast>
}

class AirqualityApiImpl(private val station: String) : AirqualityApi {

    private val client = OkHttpClient()
    private val baseUrl = "https://api.met.no/weatherapi/airqualityforecast/0.1/?station="
    private val locationsUrl = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"

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

    override fun fetchAirqualityLocations(): List<AirqualityLocation> {
        try {
            val request = Request.Builder()
                .url(locationsUrl)
                .get()
                .build()

            val response = client.newCall(request).execute()

            return parseAirqualityLocations(response)
        } catch (e: IOException) {
            Log.getStackTraceString(e)
        }
        return emptyList()
    }

    fun parseAirqualityForecastData(response: Response): ArrayList<AirqualityForecast> {
        val bodyAsJSON = JSONObject(response.body()?.string())
        val parsedData = ArrayList<AirqualityForecast>()

        val data = bodyAsJSON.getJSONObject("data").getJSONArray("time")
        val meta = bodyAsJSON.getJSONObject("meta")

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
                    AirqualityLocation("test", "test", "test"),
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

    private fun parseAirqualityLocations(response: Response): ArrayList<AirqualityLocation> {
        val bodyAsJSON = JSONArray(response.body()?.string())
        val parsedLocations = ArrayList<AirqualityLocation>()

        for (i in 0 until bodyAsJSON.length()) {
            val jsonObject = bodyAsJSON.getJSONObject(i)
            val name = jsonObject.getString("name")
            val kommune = jsonObject.getJSONObject("kommune").getString("name")
            val station = jsonObject.getString("eoi")
            parsedLocations.add(AirqualityLocation(name, kommune, station))
        }
        return parsedLocations
    }

}

