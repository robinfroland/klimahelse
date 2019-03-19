package com.example.helse

import com.example.helse.data.api.AirqualityResponse
import com.example.helse.data.api.LocationResponse
import io.kotlintest.shouldThrow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun parse_location_response_returns_formatted_location() {
        assertEquals(parsedLocationResponse, LocationResponse.parseResponse(goodLocationResponse))
    }

    @Test
    fun parse_bad_location_response_is_not_equal() {
        assertNotEquals(
            parsedLocationResponse,
            LocationResponse.parseResponse(badLocationResponse)
        )
    }

    @Test
    fun parse_wrong_location_response_throws_exception() {
        shouldThrow<Exception> {
            LocationResponse.parseResponse(wrongResponse)
        }
    }

    @Test
    fun parse_airquality_response_returns_AirqualityForecast() {
        assertEquals(
            parsedAirqualityResponse,
            AirqualityResponse.parseResponse(goodAirqualitySampleResponse, alnabruLocation)
        )
    }

    @Test
    fun parse_bad_airquality_response_throws_exception() {
        shouldThrow<Exception> {
            AirqualityResponse.parseResponse(badAirqualitySampleResponse, alnabruLocation)
        }
    }

    @Test
    fun parse_wrong_airquality_response_throws_exception() {
        shouldThrow<Exception> {
            AirqualityResponse.parseResponse(wrongResponse, alnabruLocation)
        }
    }

}
