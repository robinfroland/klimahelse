package com.example.helse.data.api

import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class AirqualityForecastApi(val location: Location) {
    private val client = OkHttpClient()

    fun fetchAirqualityFromURL(url: String): MutableList<AirqualityForecast> {
        lateinit var response: Response
        val airqualityForecastURL = buildCoordinateURI(location.latitude, location.longitude, url)
        return try {
            val request = Request.Builder()
                .url(airqualityForecastURL)
                .build()

            response = client.newCall(request).execute()

            response.parseAirqualityResponse(location)
        } catch (e: Exception) {
            println("fetchAirqualityFromURL() failed with exception $e")
            mutableListOf(emptyAirqualityForecast)
        }
    }

    private fun buildCoordinateURI(lat: Double, lon: Double, url: String): String {
        return "${url}lat=$lat&lon=$lon&areaclass=grunnkrets"
    }
}
