package com.example.helse.data.api

import com.example.helse.data.entities.*
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request

private const val HUMIDITY_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/?"

class HumidityForecastApi : RemoteForecastData<HumidityForecast> {
    private val client = OkHttpClient()

    override fun fetchForecast(location: Location): List<HumidityForecast> {
        return try {
            val uri = buildCoordinateURI(location.latitude, location.longitude)
            val request = Request.Builder()
                .url(uri)
                .build()

            val response = client.newCall(request).execute()
            response.parseHumidityResponse(location)
        } catch (e: Exception) {
            println("fetchHumidity() failed with exception $e")
            mutableListOf(emptyHumidityForecast)
        }
    }

    override fun buildCoordinateURI(latitude: Double, longitude: Double): String {
        return "${HUMIDITY_BASE_URL}lat=$latitude&lon=$longitude"
    }
}