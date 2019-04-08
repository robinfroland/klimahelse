package com.example.helse.data.api

import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.ui.uv.UvFragment
import com.example.helse.utilities.parseUvResponse
import com.example.helse.utilities.showNetworkError
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

private const val BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?station="

interface UvApi {
    fun fetchUv(url: String = BASE_URL): MutableList<UvForecast>
}

class UvResponse(
    private val location: Location,
    private val uvFragment: UvFragment
) : UvApi {

    private val client = OkHttpClient()

    override fun fetchUv(url: String): MutableList<UvForecast> {
        lateinit var response: Response
        return try {
            val request = Request.Builder()
                .url(url + location.stationID)
                .build()

            response = client.newCall(request).execute()

            response.parseUvResponse(location)
        } catch (e: Exception) {
            showNetworkError(uvFragment.requireActivity(), response.code(), e)
            mutableListOf(emptyUvForecast)
        }
    }

}
