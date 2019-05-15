package com.example.helse.data.api

import com.example.helse.data.entities.*
import com.example.helse.ui.humidity.HumidityFragment
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object HumidityForecastApi {

    private val selectedLocation = Injector.getLocation(AppContext.getAppContext())
    private val client = OkHttpClient()

    fun fetchHumidity(): MutableList<HumidityForecast> {
        lateinit var response: Response
        return try {
            val uri = fetchHumidityURI(selectedLocation.latitude, selectedLocation.longitude)
            val request = Request.Builder()
                .url(uri)
                .build()

            response = client.newCall(request).execute()

            response.parseHumidityResponse(selectedLocation)
        } catch (e: Exception) {
            mutableListOf(emptyHumidityForecast)
        }
    }

    private fun fetchHumidityURI(lat: Double, lon: Double): String {
        return "${HUMIDITY_BASE_URL}lat=$lat&lon=$lon"
    }

}