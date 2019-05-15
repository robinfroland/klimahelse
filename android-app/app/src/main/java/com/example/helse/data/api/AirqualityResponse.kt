package com.example.helse.data.api

import androidx.fragment.app.Fragment
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.parseAirqualityResponse
import com.example.helse.utilities.showNetworkError
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

private const val BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?"

interface AirqualityApi {
    fun fetchAirquality(url: String = BASE_URL): MutableList<AirqualityForecast>
}

class AirqualityResponse(
    val location: Location,
    private val fragment: Fragment
) : AirqualityApi {
    private val client = OkHttpClient()

    override fun fetchAirquality(url: String): MutableList<AirqualityForecast> {
        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(buildCoordinateURI(location.latitude, location.longitude))
                .build()

            response = client.newCall(request).execute()

            response.parseAirqualityResponse(location)
        } catch (e: Exception) {
            showNetworkError(fragment.requireActivity(), response.code(), e)
            mutableListOf(emptyAirqualityForecast)
        }
    }

    companion object {
        fun buildCoordinateURI(lat: Double, lon: Double): String {
            return "${BASE_URL}lat=$lat&lon=$lon&areaclass=grunnkrets"
        }
    }
}
