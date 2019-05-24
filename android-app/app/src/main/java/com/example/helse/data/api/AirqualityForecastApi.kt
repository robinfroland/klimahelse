package com.example.helse.data.api

import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request

private const val AIRQUALITY_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?"

class AirqualityForecastApi : RemoteForecastData<AirqualityForecast> {
    private val client = OkHttpClient()

    override fun fetchForecast(location: Location, url: String?): List<AirqualityForecast> {
        lateinit var airqualityForecastURL: String
        if (url.isNull()) {
            airqualityForecastURL = buildCoordinateURI(location.latitude, location.longitude, url)
        } else {
            airqualityForecastURL = url
        }
        
        return try {
            val uri = airqualityForecastURL
            val request = Request.Builder()
                .url(airqualityForecastURL)
                .build()

            val response = client.newCall(request).execute()
            response.parseAirqualityResponse(location)
        } catch (e: Exception) {
            println("fetchAirqualityFromURL() failed with exception $e")
            listOf(emptyAirqualityForecast)
        }
    }

    override fun buildCoordinateURI(latitude: Double, longitude: Double, url: String): String {
        return "${url}lat=$latitude&lon=$longitude&areaclass=grunnkrets"
    }
}
