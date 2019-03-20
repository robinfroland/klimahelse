package com.example.helse.data.api

import android.app.Activity
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyLocation
import com.example.helse.utilities.showNetworkError
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray

interface LocationApi {
    fun fetchAllLocations(): MutableList<Location>
}

class LocationResponse(private val activity: Activity?) : LocationApi {

    private val client = OkHttpClient()
    private val locationUrl = "https://api.met.no/weatherapi/airqualityforecast/0.1/statio"

    override fun fetchAllLocations(): MutableList<Location> {
        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(locationUrlnact)
                .get()
                .build()

            response = client.newCall(request).execute()

            response.parseResponse()
        } catch (e: Exception) {
            showNetworkError(activity, response.code(), e)
            mutableListOf(emptyLocation)
        }
    }

    private fun Response.parseResponse(): MutableList<Location> {
        val body = JSONArray(this.body()?.string())
        val parsedResponse = ArrayList<Location>()

        for (i in 0 until body.length()) {
            val locationObject = body.getJSONObject(i)
            val location = locationObject.getString("name")
            val superlocation = locationObject.getJSONObject("kommune").getString("name")
            val longitude = locationObject.getString("longitude").toDouble()
            val latitude = locationObject.getString("latitude").toDouble()
            val station = locationObject.getString("eoi")
            parsedResponse.add(
                Location(
                    location,
                    superlocation,
                    longitude,
                    latitude,
                    station
                )
            )
        }
        return parsedResponse
    }
}