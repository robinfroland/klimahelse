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
    fun fetchAirquality(): AirqualityForecast
}

class AirqualityApiImpl(private val location: AirqualityLocation) : AirqualityApi {

    private val client = OkHttpClient()
    private val baseUrl = "https://api.met.no/weatherapi/airqualityforecast/0.1/?station="
    private val locationsUrl = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"

    override fun fetchAirquality(): AirqualityForecast {

        val request = Request.Builder()
            .url(baseUrl.plus(location.station?.trim()))
            .build()

        val response = client.newCall(request).execute()

        //TODO: Gjøre strukturen mer sikker og robust

        return parseAirqualityForecastData(response)

    }

    override fun fetchAirqualityLocations(): List<AirqualityLocation> {
        try {
            val request = Request.Builder()
                .url(locationsUrl)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                print("responseCode: ${response.code()}")
                throw Error("Something went wrong, error code is not 200 ${response.message()}")
            }

            return parseAirqualityLocations(response)
        } catch (e: IOException) {
            Log.getStackTraceString(e)
        }
        return emptyList()
    }

    private fun parseAirqualityForecastData(response: Response): AirqualityForecast {
        val bodyAsJSON = JSONObject(response.body()?.string())

        val data = bodyAsJSON.getJSONObject("data").getJSONArray("time")

        //TODO: implement userdefined timeframe for forecast. Index 0 temporarily
        val jsonObj = data.getJSONObject(0)
        val to = jsonObj.getString("to")
        val from = jsonObj.getString("from")

        val variables = jsonObj.getJSONObject("variables")
        val o3Concentration = variables.getJSONObject("o3_concentration").getDouble("value")
        val pm10Concentration = variables.getJSONObject("pm10_concentration").getDouble("value")
        val pm25Concentration = variables.getJSONObject("pm25_concentration").getDouble("value")
        val no2Concentration = variables.getJSONObject("no2_concentration").getDouble("value")

        return AirqualityForecast(
            location,
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

