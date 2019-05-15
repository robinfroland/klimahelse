package com.example.helse.data.api

import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyLocation
import com.example.helse.utilities.STATIONS_BASE_URL
import com.example.helse.utilities.parseLocationResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object LocationApi {

    fun fetchAllLocations(): MutableList<Location> {
        val client = OkHttpClient()

        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(STATIONS_BASE_URL)
                .get()
                .build()

            response = client.newCall(request).execute()

            response.parseLocationResponse()
        } catch (e: Exception) {
            mutableListOf(emptyLocation)
        }
    }
}
