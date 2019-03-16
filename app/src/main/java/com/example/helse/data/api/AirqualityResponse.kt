package com.example.helse.data.api

import android.app.Activity
import com.example.helse.data.entities.*
import com.example.helse.utilities.showNetworkError
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

interface AirqualityApi {
    fun fetchAirquality(): AirqualityForecast
}

class AirqualityResponse(
    private val location: Location,
    private val airqualityActivity: Activity
) : AirqualityApi {

    private val client = OkHttpClient()
    private val baseURL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?stations="

    override fun fetchAirquality(): AirqualityForecast {
        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(baseURL + location.stationID)
                .build()

            response = client.newCall(request).execute()

            response.parseResponse()
        } catch (e: Exception) {
            showNetworkError(airqualityActivity, response.code())
            emptyAirqualityForecast
        }
    }

    private fun Response.parseResponse(): AirqualityForecast {
        val bodyAsJSON = JSONObject(this.body()?.string())

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
}

