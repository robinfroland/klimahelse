package com.example.helse.data.api

import android.util.Log
import com.example.helse.data.entities.Location
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

interface LocationApi {
    fun fetchAllLocations(): MutableList<Location>
}

class LocationResponse : LocationApi {

    private val client = OkHttpClient()
    private val locationUrl = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"
    private lateinit var locations: MutableList<Location>

    override fun fetchAllLocations(): MutableList<Location> {
        try {
            val request = Request.Builder()
                .url(locationUrl)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                print("responseCode: ${response.code()}")
                throw Error("Something went wrong, error code is not 200 ${response.message()}")
            }

            locations = response.parseResponse()

        } catch (e: IOException) {
            Log.getStackTraceString(e)
        }
        return locations
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