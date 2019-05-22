package com.example.helse.data.api

import com.example.helse.data.entities.*
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

private const val HUMIDITY_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/?"

class HumidityForecastApi : RemoteForecastData<HumidityForecast> {
    override fun buildCoordinateURI(latitude: Double, longitude: Double): String {
        return "${HUMIDITY_BASE_URL}lat=$latitude&lon=$longitude"
    }


    private val client = OkHttpClient()

    override fun fetchForecast(location: Location): List<HumidityForecast> {
        lateinit var response: Response
        return try {
            val uri = createHumidityURI(location.latitude, location.longitude)
            val request = Request.Builder()
                .url(uri)
                .build()

            response = client.newCall(request).execute()

            val res = response.parseHumidityResponse(location)
            res
        } catch (e: Exception) {
            println("fetchHumidity() failed with exception $e")
            mutableListOf(emptyHumidityForecast)
        }
    }

    private fun createHumidityURI(lat: Double, lon: Double): String {
        return "${HUMIDITY_BASE_URL}lat=$lat&lon=$lon"
    }
}