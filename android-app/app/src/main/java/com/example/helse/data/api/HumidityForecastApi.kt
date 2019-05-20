package com.example.helse.data.api

import com.example.helse.data.entities.*
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class HumidityForecastApi(val location: Location) {
    private val client = OkHttpClient()

    fun fetchHumidity(): MutableList<HumidityForecast> {
        lateinit var response: Response
        return try {
            val uri = createHumidityURI(location.latitude, location.longitude)
            val request = Request.Builder()
                .url(uri)
                .build()

            response = client.newCall(request).execute()
            println("response $response")


            val res = response.parseHumidityResponse(location)
            println("Res from parseHumidity = $res")
            res
        } catch (e: Exception) {
            println("Failed with exception $e")
            mutableListOf(emptyHumidityForecast)
        }
    }

    private val HUMIDITY_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/?"
    private fun createHumidityURI(lat: Double, lon: Double): String {
        return "${HUMIDITY_BASE_URL}lat=$lat&lon=$lon"
    }
}