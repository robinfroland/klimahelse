package com.example.helse.data.repositories


import androidx.test.core.app.ApplicationProvider
import com.example.helse.alnabruLocation
import com.example.helse.data.api.AirqualityForecastApi
import com.example.helse.data.database.LocalDatabase
import com.example.helse.goodAirqualitySampleResponse

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(
    RobolectricTestRunner::class
)


class AirqualityForecastRepositoryTest {
    private val server = MockWebServer()
    private val serverUrl = server.url("/").toString()


    @Test
    fun airquality_forecast_add_and_get(){
        server.enqueue(MockResponse().setBody(goodAirqualitySampleResponse))


        val airqualityRepository =
            AirqualityForecastRepository(
                LocalDatabase.getInstance(ApplicationProvider.getApplicationContext()).airqualityDao(),
                AirqualityForecastApi(alnabruLocation)
            )

        val parsedResponse = airqualityRepository.fetchAirquality()

        assertEquals(parsedResponse, goodAirqualitySampleResponse)


    }

}