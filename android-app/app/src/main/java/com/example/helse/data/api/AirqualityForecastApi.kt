package com.example.helse.data.api

import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

private const val AIRQUALITY_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?"

class AirqualityForecastApi : RemoteForecastData<AirqualityForecast> {

    private val client = OkHttpClient()

    override fun fetchForecast(location: Location): MutableList<AirqualityForecast> {
        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(buildCoordinateURI(location.latitude, location.longitude))
                .build()

            response = client.newCall(request).execute()

            response.parseAirqualityResponse(location)
        } catch (e: Exception) {
            println("fetchAirqualityFromURL() failed with exception $e")
            mutableListOf(emptyAirqualityForecast)
        }
    }

    override fun buildCoordinateURI(latitude: Double, longitude: Double): String {
        return "${AIRQUALITY_BASE_URL}lat=$latitude&lon=$longitude&areaclass=grunnkrets"
    }


}
