package com.example.helse.data.api

import android.content.Context
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.ui.airquality.AirqualityFragment
import com.example.helse.utilities.parseAirqualityResponse
import com.example.helse.utilities.showNetworkError
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.security.AccessControlContext

private const val BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?station="

interface AirqualityApi {
    fun fetchAirquality(url: String = BASE_URL): AirqualityForecast
}

class AirqualityResponse(
    private val location: Location,
    private val airqualityFragment: AirqualityFragment
) : AirqualityApi {

    private val client = OkHttpClient()

    override fun fetchAirquality(url: String): AirqualityForecast {
        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(url + location.stationID)
                .build()

            response = client.newCall(request).execute()

            response.parseAirqualityResponse(location)
        } catch (e: Exception) {
            showNetworkError(airqualityFragment.requireActivity(), response.code(), e)
            emptyAirqualityForecast
        }
    }

}
