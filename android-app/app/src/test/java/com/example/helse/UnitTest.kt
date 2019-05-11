package com.example.helse

import com.example.helse.data.api.AirqualityResponse
import com.example.helse.data.api.LocationResponse
import com.example.helse.ui.airquality.AirqualityFragment
import io.kotlintest.shouldThrow
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
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
class UnitTest {
    @Test
    fun parse_location_response_returns_formatted_location() {
        val server = MockWebServer()
        val serverUrl = server.url("/")
        server.enqueue(MockResponse().setBody(goodLocationResponse))

        val parsedResponse = LocationResponse(null).fetchAllLocations(serverUrl.toString())

        assertEquals(parsedLocationResponse, parsedResponse)

        server.shutdown()
    }

    @Test
    fun parse_bad_location_response_is_not_equal_to_sample() {
        val server = MockWebServer()
        val serverUrl = server.url("/")
        server.enqueue(MockResponse().setBody(badLocationResponse))

        val parsedResponse = LocationResponse(null).fetchAllLocations(serverUrl.toString())

        assertNotEquals(
            parsedLocationResponse,
            parsedResponse
        )

        server.shutdown()
    }

    @Test
    fun parse_wrong_location_response_throws_exception() {
        val server = MockWebServer()
        val serverUrl = server.url("/")
        server.enqueue(MockResponse().setBody(wrongResponse))

        shouldThrow<Error> {
            LocationResponse(null).fetchAllLocations(serverUrl.toString())
        }

        server.shutdown()
    }


    @Test
    fun parse_airquality_response_returns_AirqualityForecast() {
        val server = MockWebServer()
        val serverUrl = server.url("/")
        server.enqueue(MockResponse().setBody(goodAirqualitySampleResponse))

        assertEquals(
            parsedAirqualityResponse,
            AirqualityResponse(
                alnabruLocation,
                AirqualityFragment()
            ).fetchAirquality(
                serverUrl.toString()
            )
        )
    }

    @Test
    fun parse_bad_airquality_response_is_not_equal_to_sample() {
        val server = MockWebServer()
        val serverUrl = server.url("/")
        server.enqueue(MockResponse().setBody(badAirqualitySampleResponse))

        assertNotEquals(
            parsedAirqualityResponse,
            AirqualityResponse(alnabruLocation, AirqualityFragment()).fetchAirquality(serverUrl.toString())
        )
    }

    @Test
    fun parse_wrong_airquality_response_throws_exception() {
        val server = MockWebServer()
        val serverUrl = server.url("/")
        server.enqueue(MockResponse().setBody(wrongResponse))

        shouldThrow<Exception> {
            AirqualityResponse(alnabruLocation, AirqualityFragment()).fetchAirquality(serverUrl.toString())
        }
    }
}