package com.example.helse.data.api

import com.example.helse.data.entities.*
import com.example.helse.ui.humidity.HumidityFragment
import com.example.helse.utilities.parseHumidityResponse
import com.example.helse.utilities.showNetworkError
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

private const val BASE_URL = "https://api.met.no/weatherapi/locationforecast/1.9/?"

interface HumidityApi {
    fun fetchHumidity(): MutableList<HumidityForecast>
}

class HumidityResponse(
    private val location: Location
) : HumidityApi {

    private val client = OkHttpClient()

    override fun fetchHumidity(): MutableList<HumidityForecast> {
        lateinit var response: Response
        return try {
            val uri = fetchHumidityURI(location.latitude, location.longitude)
            val request = Request.Builder()
                .url(uri)
                .build()

            response = client.newCall(request).execute()

            response.parseHumidityResponse(location)
        } catch (e: Exception) {
            mutableListOf(emptyHumidityForecast)
        }
    }

    companion object {
        fun fetchHumidityURI(lat: Double, lon: Double): String {
            return "${BASE_URL}lat=$lat&lon=$lon"
        }
    }
}