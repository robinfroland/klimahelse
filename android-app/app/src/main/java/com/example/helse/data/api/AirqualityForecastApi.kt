package com.example.helse.data.api

import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

private const val AIRQUALITY_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?"

class AirqualityForecastApi(val location: Location) {
    private val client = OkHttpClient()

    private val airqualityForecastURL = buildCoordinateURI(location.latitude, location.longitude)
    fun fetchAirqualityFromURL(url: String = airqualityForecastURL): MutableList<AirqualityForecast> {
        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(url)
                .build()

            response = client.newCall(request).execute()

            response.parseAirqualityResponse(location)
        } catch (e: Exception) {
            println("fetchAirqualityFromURL() failed with exception $e")
            mutableListOf(emptyAirqualityForecast)
        }
    }

    private fun buildCoordinateURI(lat: Double, lon: Double): String {
        return "${AIRQUALITY_BASE_URL}lat=$lat&lon=$lon&areaclass=grunnkrets"
    }
}
