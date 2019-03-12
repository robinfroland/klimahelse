package com.example.helse.data.api

import android.util.Log
import com.example.helse.data.entities.Airquality
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.AirqualityVariables
import com.example.helse.data.entities.Location
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

interface AirqualityApi {
    fun fetchAirquality(): AirqualityForecast
}

class AirqualityResponse(
    private val location: Location
) : AirqualityApi {

    private val client = OkHttpClient()
    private val baseURL = "https://api.met.no/weatherapi/airqualityforecast/0.1/?station="
    private lateinit var airqualityForecast: AirqualityForecast

    override fun fetchAirquality(): AirqualityForecast {
        try {
            val request = Request.Builder()
                .url("${baseURL}NO0057A")
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                print("responseCode: ${response.code()}")
                throw Error("Something went wrong, error code is not 200 ${response.message()}")
            }

            airqualityForecast = response.parseResponse()

        } catch (e: IOException) {
            Log.getStackTraceString(e)
        }
        return airqualityForecast
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

