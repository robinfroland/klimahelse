package com.example.helse.data.api

import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyLocation
import com.example.helse.utilities.parseLocationResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object LocationApi {
    private const val STATIONS_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/stations"

    fun fetchAllLocations(url: String = STATIONS_BASE_URL): MutableList<Location> {
        val client = OkHttpClient()

        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            response = client.newCall(request).execute()

            response.parseLocationResponse()
        } catch (e: Exception) {
            mutableListOf(emptyLocation)
        }
    }
}
