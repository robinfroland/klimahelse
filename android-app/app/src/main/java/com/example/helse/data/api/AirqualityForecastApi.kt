package com.example.helse.data.api

import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object AirqualityForecastApi {

    private val selectedLocation = Injector.getLocation(AppContext.getAppContext())
    private val client = OkHttpClient()

    fun fetchAirquality(): MutableList<AirqualityForecast> {
        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(buildCoordinateURI(selectedLocation.latitude, selectedLocation.longitude))
                .build()

            response = client.newCall(request).execute()

            response.parseAirqualityResponse(selectedLocation)
        } catch (e: Exception) {
            mutableListOf(emptyAirqualityForecast)
        }
    }

    private fun buildCoordinateURI(lat: Double, lon: Double): String {
        return "${AIRQUALITY_BASE_URL}lat=$lat&lon=$lon&areaclass=grunnkrets"
    }
}
