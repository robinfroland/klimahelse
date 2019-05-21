package com.example.helse.data.api

import com.example.helse.utilities.parseUvResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.junit.Test
import com.example.helse.alnabruLocation
import com.example.helse.goodAirqualitySampleResponse
import com.example.helse.parsedUvResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(
    RobolectricTestRunner::class
)
class UvForecastApiTest {
    private val selectedLocation = alnabruLocation
    private val client = OkHttpClient()

    @Test
    fun fetchUv() {
        val server = MockWebServer()
        val serverUrl = server.url("/")
        server.enqueue(MockResponse().setBody(goodAirqualitySampleResponse))
        lateinit var response: Response

        // Get URI's for today, tomorrow, and overtomorrow
        val request = Request.Builder()
            .url(serverUrl)
            .build()

        response = client.newCall(request).execute()

        assertEquals(parsedUvResponse, response.parseUvResponse(selectedLocation))

    }
}