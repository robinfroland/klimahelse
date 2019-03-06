package com.example.helse.forecast

import okhttp3.OkHttpClient
import okhttp3.Request

interface AirqualityForecastApi {
    fun fetchAirquality(): List<Any>
}

class AirqualityForecastApiImpl : AirqualityForecastApi {

    private val client = OkHttpClient()
    private val url = "https://api.met.no/weatherapi/airqualityforecast/0.1/met?station=NO0057A"

    override fun fetchAirquality(): List<Any> {
        val request = Request.Builder()
            .url(url)
            .build()

        print("responseMoi")
        val response = client.newCall(request).execute()

        val response2 = response.body()
        var response3 = ""
        if (response2 != null) {
            response3 = response2.string()
        }
        println("response1 $response")
        println("response2 $response2")
        println("response3 $response3")

        val forecast = ArrayList<Any>()

        // Parse the data here, look at Arans PR

        return forecast
    }
}
