package com.example.helse

import com.example.helse.data.api.AirqualityForecastApi
import com.example.helse.data.api.LocationApi
import com.example.helse.data.entities.*
import com.example.helse.ui.airquality.AirqualityFragment
import io.kotlintest.shouldNotThrow
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

const val goodLocationResponse =
    "[{\"eoi\": \"NO0057A\", \"latitude\": \"59.92767\", \"height\": 3.0, \"delomrade\": {\"name\": \"Alfaset\", \"areacode\": \"03013700\"}, \"grunnkrets\": {\"name\": \"Alfaset\", \"areacode\": \"03013701\"}, \"name\": \"Alnabru\", \"kommune\": {\"name\": \"Oslo\", \"areacode\": \"0301\"}, \"longitude\": \"10.84655\"}, {\"eoi\": \"NO0110A\", \"latitude\": \"59.27378\", \"height\": 3.0, \"delomrade\": {\"name\": \"Sarpsborg vest\", \"areacode\": \"01050100\"}, \"grunnkrets\": {\"name\": \"Alvimhaugen\", \"areacode\": \"01050102\"}, \"name\": \"Alvim\", \"kommune\": {\"name\": \"Sarpsborg\", \"areacode\": \"0105\"}, \"longitude\": \"11.08919\"}]"

// The first name is what makes it bad
const val badLocationResponse =
    "[{\"eoi\": \"NO0057A\", \"latitude\": \"59.92767\", \"height\": 3.0, \"delomrade\": {\"name\": \"DETTEERIKKERIKTIGLOL\", \"areacode\": \"03013700\"}, \"grunnkrets\": {\"name\": \"Alfaset\", \"areacode\": \"03013701\"}, \"name\": \"JASÅ JA DU TROR DET ER ALNABRU\", \"kommune\": {\"name\": \"Oslo\", \"areacode\": \"0301\"}, \"longitude\": \"10.84655\"}, {\"eoi\": \"NO0110A\", \"latitude\": \"59.27378\", \"height\": 3.0, \"delomrade\": {\"name\": \"Sarpsborg vest\", \"areacode\": \"01050100\"}, \"grunnkrets\": {\"name\": \"Alvimhaugen\", \"areacode\": \"01050102\"}, \"name\": \"Alvim\", \"kommune\": {\"name\": \"Sarpsborg\", \"areacode\": \"0105\"}, \"longitude\": \"11.08919\"}]"

val alnabruLocation = Location(
    location = "Alnabru",
    superlocation = "Oslo",
    latitude = 59.92767,
    longitude = 10.84655,
    stationID =
    "NO0057A"
)

const val goodAirqualitySampleResponse =
    "{\"data\": {\"time\": [{\"variables\": {\"no2_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_concentration\": {\"units\": \"ug/m3\", \"value\": 1.0733610391616821}, \"o3_concentration\": {\"units\": \"ug/m3\", \"value\": 68.15038299560547}, \"AQI_no2\": {\"units\": \"1\", \"value\": 1.0}, \"pm10_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 5.0}, \"no2_local_fraction_heating\": {\"units\": \"%\", \"value\": 0.0}, \"pm10_local_fraction_heating\": {\"units\": \"%\", \"value\": 8.0}, \"AQI_o3\": {\"units\": \"1\", \"value\": 1.6}, \"o3_nonlocal_fraction\": {\"units\": \"%\", \"value\": 100.0}, \"pm25_local_fraction_heating\": {\"units\": \"%\", \"value\": 11.0}, \"no2_nonlocal_fraction\": {\"units\": \"%\", \"value\": 32.0}, \"pm10_local_fraction_traffic_nonexhaust\": {\"units\": \"%\", \"value\": 0.0}, \"AQI\": {\"units\": \"1\", \"value\": 1.6}, \"pm25_local_fraction_traffic_nonexhaust\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"no2_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 66.0}, \"pm10_concentration\": {\"units\": \"ug/m3\", \"value\": 1.5612730979919434}, \"no2_concentration\": {\"units\": \"ug/m3\", \"value\": 3.354196071624756}, \"AQI_pm10\": {\"units\": \"1\", \"value\": 1.0}, \"pm10_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"pm10_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 7.0}, \"pm10_nonlocal_fraction\": {\"units\": \"%\", \"value\": 85.0}, \"AQI_pm25\": {\"units\": \"1\", \"value\": 1.0}, \"no2_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_nonlocal_fraction\": {\"units\": \"%\", \"value\": 80.0}}, \"to\": \"2019-03-18T01:00:00Z\", \"from\": \"2019-03-18T01:00:00Z\"}]}, \"meta\": {\"superlocation\": {\"areaclass\": \"kommune\", \"areacode\": \"0301\", \"superareacode\": \"03\", \"name\": \"Oslo\", \"longitude\": \"10.84655\", \"latitude\": \"59.92767\"}, \"reftime\": \"2019-03-18T12:00:00Z\", \"sublocations\": [], \"location\": {\"name\": \"Alnabru\", \"areacode\": \"NO0057A\", \"latitude\": \"10.84655\", \"longitude\": \"59.92767\"}}}"

// The insane pm25_concentration is what makes it bad
const val badAirqualitySampleResponse =
    "{\"data\": {\"time\": [{\"variables\": {\"no2_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_concentration\": {\"units\": \"ug/m3\", \"value\": 19321.0391616821}, \"o3_concentration\": {\"units\": \"ug/m3\", \"value\": 68.15038299560547}, \"AQI_no2\": {\"units\": \"1\", \"value\": 1.0}, \"pm10_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 5.0}, \"no2_local_fraction_heating\": {\"units\": \"%\", \"value\": 0.0}, \"pm10_local_fraction_heating\": {\"units\": \"%\", \"value\": 8.0}, \"AQI_o3\": {\"units\": \"1\", \"value\": 1.6}, \"o3_nonlocal_fraction\": {\"units\": \"%\", \"value\": 100.0}, \"pm25_local_fraction_heating\": {\"units\": \"%\", \"value\": 11.0}, \"no2_nonlocal_fraction\": {\"units\": \"%\", \"value\": 32.0}, \"pm10_local_fraction_traffic_nonexhaust\": {\"units\": \"%\", \"value\": 0.0}, \"AQI\": {\"units\": \"1\", \"value\": 1.6}, \"pm25_local_fraction_traffic_nonexhaust\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"no2_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 66.0}, \"pm10_concentration\": {\"units\": \"ug/m3\", \"value\": 1.5612730979919434}, \"no2_concentration\": {\"units\": \"ug/m3\", \"value\": 3.354196071624756}, \"AQI_pm10\": {\"units\": \"1\", \"value\": 1.0}, \"pm10_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"pm10_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 7.0}, \"pm10_nonlocal_fraction\": {\"units\": \"%\", \"value\": 85.0}, \"AQI_pm25\": {\"units\": \"1\", \"value\": 1.0}, \"no2_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_nonlocal_fraction\": {\"units\": \"%\", \"value\": 80.0}}, \"to\": \"2019-03-18T01:00:00Z\", \"from\": \"2019-03-18T01:00:00Z\"}]}, \"meta\": {\"superlocation\": {\"areaclass\": \"kommune\", \"areacode\": \"0301\", \"superareacode\": \"03\", \"name\": \"Oslo\", \"longitude\": \"10.84655\", \"latitude\": \"59.92767\"}, \"reftime\": \"2019-03-18T12:00:00Z\", \"sublocations\": [], \"location\": {\"name\": \"Alnabru\", \"areacode\": \"NO0057A\", \"latitude\": \"10.84655\", \"longitude\": \"59.92767\"}}}"

const val goodUVSampleResponse = ""
const val badUVSampleResponse = ""


const val goodHumiditySampleResponse = ""
const val badHumiditySampleResponse = ""

const val wrongResponse = "{\"message\": \"undefined station requested\"}"

val parsedAirqualityResponse = mutableListOf(AirqualityForecast(
    stationID = alnabruLocation.stationID,
    from = "2019-03-18T01:00:00Z",
    to = "2019-03-18T01:00:00Z",
    riskValue = "LAV RISIKO",
    o3_concentration = 68.15038299560547,
    o3_riskValue = "LAV RISIKO",
    pm10_concentration = 1.5612730979919434,
    pm10_riskValue = "LAV RISIKO",
    pm25_concentration = 1.0733610391616821,
    pm25_riskValue = "LAV RISIKO",
    no2_concentration = 3.354196071624756,
    no2_riskValue = "LAV RISIKO"
))

val parsedUvResponse = mutableListOf(UvForecast(
    latitude = 59.92767,
    longitude = 10.84655,
    uvClear = 4.64,
    uvCloudy = 4.21,
    uvPartlyCloudy= 3.94,
    uvForecast = 0.81,
    riskValue = "LAV RISIKO"
))

val parsedHumidityResponse = mutableListOf(HumidityForecast(
    from = "2019-03-18T01:00:00Z",
    riskValue = "HØY LUFTFUKTIGHET",
    latitude = 59.9277,
    longitude = 10.8465,
    humidityValue = 91.1,
    temperature = 21.0
))

val parsedLocationResponse = mutableListOf(
    Location(location =  "Alnabru",superlocation =  "Oslo",latitude =  59.92767, longitude = 10.84655, stationID =  "NO0057A"),
    Location(location = "Alvim",superlocation =  "Sarpsborg", latitude =  59.27378,longitude = 11.08919, stationID =  "NO0110A")
)


@RunWith(
    RobolectricTestRunner::class
)
class UnitTest {
    private val server = MockWebServer()
    private val serverUrl = server.url("/").toString()

    @Test
    fun parse_location_response_returns_formatted_location() {
        server.enqueue(MockResponse().setBody(goodLocationResponse))

        val parsedResponse = LocationApi.fetchAllLocations(serverUrl)

        assertEquals(parsedLocationResponse, parsedResponse)
    }

    @Test
    fun parse_bad_location_response_is_not_equal_to_sample() {
        server.enqueue(MockResponse().setBody(badLocationResponse))

        val parsedResponse = LocationApi.fetchAllLocations(serverUrl)

        assertNotEquals(
            parsedLocationResponse,
            parsedResponse
        )
    }

    @Test
    fun parse_wrong_location_response_throws_does_not_throw_exception() {
        server.enqueue(MockResponse().setBody(wrongResponse))

        shouldNotThrow<Error> {
            LocationApi.fetchAllLocations(serverUrl)
        }
    }


    @Test
    fun parse_airquality_response_returns_AirqualityForecast() {
        server.enqueue(MockResponse().setBody(goodAirqualitySampleResponse))

        val parsedResponse = AirqualityForecastApi(alnabruLocation).fetchAirqualityFromURL(serverUrl)

        assertEquals(
            parsedAirqualityResponse,
            parsedResponse
        )
    }

    @Test
    fun parse_bad_airquality_response_is_not_equal_to_sample() {
        server.enqueue(MockResponse().setBody(badAirqualitySampleResponse))

        val parsedResponse = AirqualityForecastApi(alnabruLocation).fetchAirqualityFromURL(serverUrl)

        assertNotEquals(
            parsedAirqualityResponse,
            parsedResponse
        )
    }

    @Test
    fun parse_wrong_airquality_response_returns_empty_forecast() {
        server.enqueue(MockResponse().setBody(wrongResponse))

        val parsedResponse = AirqualityForecastApi(alnabruLocation).fetchAirqualityFromURL(serverUrl)

        assertEquals(
            mutableListOf(emptyAirqualityForecast),
            parsedResponse
        )
    }
    @Test
    fun parse_wrong_airquality_response_throws_does_not_throw_exception() {
        server.enqueue(MockResponse().setBody(wrongResponse))

        shouldNotThrow<Error> {
            AirqualityForecastApi(alnabruLocation).fetchAirqualityFromURL(serverUrl)
        }
    }
}