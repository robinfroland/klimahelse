package com.example.helse.data.api

import android.app.Activity
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyLocation
import com.example.helse.utilities.parseLocationResponse
import com.example.helse.utilities.showNetworkError
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

private const val LOCATION_URL = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"

interface LocationApi {
    fun fetchAllLocations(url: String = LOCATION_URL): MutableList<Location>
}

class LocationResponse(private val activity: Activity?) : LocationApi {
    override fun fetchAllLocations(url: String): MutableList<Location> {
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
            showNetworkError(activity, response.code(), e)
            mutableListOf(emptyLocation)
        }
    }
}
