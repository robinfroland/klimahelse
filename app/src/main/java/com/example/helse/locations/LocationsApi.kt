package com.example.helse.locations

import android.util.Log
import okhttp3.Request
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

interface LocationsApi {
    fun fetchLocations(): List<Locations>
}

class LocationsApiImpl : LocationsApi {

    private val client = OkHttpClient()
    private val locationsUrl = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"

    override fun fetchLocations(): List<Locations> {
        try {
            val request = Request.Builder()
                .url(locationsUrl)
                .get()
                .build()

            val response = client.newCall(request).execute()

            return parseLocations(response)
        } catch (e: IOException) {
            Log.getStackTraceString(e)
        }
        return emptyList()
    }

    private fun parseLocations(response: Response): ArrayList<Locations> {
        val bodyAsJSON = JSONArray(response.body()?.string())
        val parsedLocations = ArrayList<Locations>()

        for (i in 0 until bodyAsJSON.length()) {
            val jsonObject = bodyAsJSON.getJSONObject(i)
            val name = jsonObject.getString("name")
            val kommuneName = jsonObject.getJSONObject("kommune").getString("name")
            val stasjon = jsonObject.getString("eoi")
            parsedLocations.add(Locations(name, kommuneName, stasjon))
        }
        return parsedLocations
    }
}
