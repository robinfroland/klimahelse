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

const val goodUVSampleResponse = "<weatherdata xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" created=\"2019-05-21T19:43:52Z\" xsi:noNamespaceSchemaLocation=\"https://api.met.no/weatherapi/uvforecast/1.0/schema\"><product class=\"uvforecast\"><time from=\"2019-05-22T12:00:00Z\" to=\"2019-05-22T12:00:00Z\"><location latitude=\"63.75\" longitude=\"22.50\" altitude=\"0.00\"><uv><uvi_clear unit=\"uv-index\" value=\"3.99\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"3.39\"/><uvi_cloudy unit=\"uv-index\" value=\"1.04\"/><uvi_forecast unit=\"uv-index\" value=\"1.04\"/><ozon unit=\"kg/m^-2\" value=\"7.475920e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"8.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"43.37\"/></uv></location><location latitude=\"67.50\" longitude=\"15.75\" altitude=\"0.38\"><uv><uvi_clear unit=\"uv-index\" value=\"3.23\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"2.74\"/><uvi_cloudy unit=\"uv-index\" value=\"0.84\"/><uvi_forecast unit=\"uv-index\" value=\"2.74\"/><ozon unit=\"kg/m^-2\" value=\"7.888801e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"4.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"47.12\"/></uv></location><location latitude=\"62.50\" longitude=\"6.25\" altitude=\"0.04\"><uv><uvi_clear unit=\"uv-index\" value=\"4.43\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"3.77\"/><uvi_cloudy unit=\"uv-index\" value=\"1.15\"/><uvi_forecast unit=\"uv-index\" value=\"2.74\"/><ozon unit=\"kg/m^-2\" value=\"7.181413e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"6.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"42.12\"/></uv></location><location latitude=\"61.50\" longitude=\"22.00\" altitude=\"0.04\"><uv><uvi_clear unit=\"uv-index\" value=\"4.46\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"3.79\"/><uvi_cloudy unit=\"uv-index\" value=\"1.16\"/><uvi_forecast unit=\"uv-index\" value=\"1.16\"/><ozon unit=\"kg/m^-2\" value=\"7.391579e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"8.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"41.12\"/></uv></location><location latitude=\"65.25\" longitude=\"12.00\" altitude=\"0.03\"><uv><uvi_clear unit=\"uv-index\" value=\"4.03\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"3.42\"/><uvi_cloudy unit=\"uv-index\" value=\"1.05\"/><uvi_forecast unit=\"uv-index\" value=\"1.05\"/><ozon unit=\"kg/m^-2\" value=\"7.025547e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"8.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"44.87\"/></uv></location><location latitude=\"67.50\" longitude=\"23.50\" altitude=\"0.20\"><uv><uvi_clear unit=\"uv-index\" value=\"3.17\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"2.69\"/><uvi_cloudy unit=\"uv-index\" value=\"0.82\"/><uvi_forecast unit=\"uv-index\" value=\"3.15\"/><ozon unit=\"kg/m^-2\" value=\"7.914133e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"1.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"47.12\"/></uv></location></product></weatherdata>"
// The first latitude in this response is different
const val badUVSampleResponse = "<weatherdata xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" created=\"2019-05-21T19:43:52Z\" xsi:noNamespaceSchemaLocation=\"https://api.met.no/weatherapi/uvforecast/1.0/schema\"><product class=\"uvforecast\"><time from=\"2019-05-22T12:00:00Z\" to=\"2019-05-22T12:00:00Z\"><location latitude=\"69.75\" longitude=\"22.50\" altitude=\"0.00\"><uv><uvi_clear unit=\"uv-index\" value=\"3.99\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"3.39\"/><uvi_cloudy unit=\"uv-index\" value=\"1.04\"/><uvi_forecast unit=\"uv-index\" value=\"1.04\"/><ozon unit=\"kg/m^-2\" value=\"7.475920e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"8.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"43.37\"/></uv></location><location latitude=\"67.50\" longitude=\"15.75\" altitude=\"0.38\"><uv><uvi_clear unit=\"uv-index\" value=\"3.23\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"2.74\"/><uvi_cloudy unit=\"uv-index\" value=\"0.84\"/><uvi_forecast unit=\"uv-index\" value=\"2.74\"/><ozon unit=\"kg/m^-2\" value=\"7.888801e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"4.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"47.12\"/></uv></location><location latitude=\"62.50\" longitude=\"6.25\" altitude=\"0.04\"><uv><uvi_clear unit=\"uv-index\" value=\"4.43\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"3.77\"/><uvi_cloudy unit=\"uv-index\" value=\"1.15\"/><uvi_forecast unit=\"uv-index\" value=\"2.74\"/><ozon unit=\"kg/m^-2\" value=\"7.181413e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"6.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"42.12\"/></uv></location><location latitude=\"61.50\" longitude=\"22.00\" altitude=\"0.04\"><uv><uvi_clear unit=\"uv-index\" value=\"4.46\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"3.79\"/><uvi_cloudy unit=\"uv-index\" value=\"1.16\"/><uvi_forecast unit=\"uv-index\" value=\"1.16\"/><ozon unit=\"kg/m^-2\" value=\"7.391579e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"8.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"41.12\"/></uv></location><location latitude=\"65.25\" longitude=\"12.00\" altitude=\"0.03\"><uv><uvi_clear unit=\"uv-index\" value=\"4.03\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"3.42\"/><uvi_cloudy unit=\"uv-index\" value=\"1.05\"/><uvi_forecast unit=\"uv-index\" value=\"1.05\"/><ozon unit=\"kg/m^-2\" value=\"7.025547e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"8.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"44.87\"/></uv></location><location latitude=\"67.50\" longitude=\"23.50\" altitude=\"0.20\"><uv><uvi_clear unit=\"uv-index\" value=\"3.17\"/><uvi_partly_cloudy unit=\"uv-index\" value=\"2.69\"/><uvi_cloudy unit=\"uv-index\" value=\"0.82\"/><uvi_forecast unit=\"uv-index\" value=\"3.15\"/><ozon unit=\"kg/m^-2\" value=\"7.914133e-03\"/><snowcover unit=\"percentage\" value=\"0.00\"/><cloud_cover unit=\"parts of 8\" value=\"1.00\"/><albedo unit=\"percentage\" value=\"0.05\"/><solar_zenith unit=\"angle\" value=\"47.12\"/></uv></location></product></weatherdata>"


const val goodHumiditySampleResponse = "<weatherdata xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://api.met.no/weatherapi/locationforecast/1.9/schema\" created=\"2019-05-21T19:43:42Z\"><meta><model name=\"LOCAL\" termin=\"2019-05-21T12:00:00Z\" runended=\"2019-05-21T14:30:11Z\" nextrun=\"2019-05-21T22:00:00Z\" from=\"2019-05-21T20:00:00Z\" to=\"2019-05-24T06:00:00Z\"/><model name=\"EPS\" termin=\"2019-05-21T00:00:00Z\" runended=\"2019-05-21T09:04:59Z\" nextrun=\"2019-05-21T22:00:00Z\" from=\"2019-05-24T12:00:00Z\" to=\"2019-05-30T18:00:00Z\"/></meta><product class=\"pointData\"><time datatype=\"forecast\" from=\"2019-05-21T20:00:00Z\" to=\"2019-05-21T20:00:00Z\"><location altitude=\"485\" latitude=\"60.1000\" longitude=\"9.5800\"><temperature id=\"TTT\" unit=\"celsius\" value=\"13.9\"/><windDirection id=\"dd\" deg=\"36.5\" name=\"NE\"/><windSpeed id=\"ff\" mps=\"0.8\" beaufort=\"1\" name=\"Flau vind\"/><windGust id=\"ff_gust\" mps=\"4.9\"/><areaMaxWindSpeed mps=\"3.0\"/><humidity value=\"99.1\" unit=\"percent\"/><pressure id=\"pr\" unit=\"hPa\" value=\"1009.7\"/><cloudiness id=\"NN\" percent=\"100.0\"/><fog id=\"FOG\" percent=\"6.0\"/><lowClouds id=\"LOW\" percent=\"52.9\"/><mediumClouds id=\"MEDIUM\" percent=\"97.6\"/><highClouds id=\"HIGH\" percent=\"99.5\"/><dewpointTemperature id=\"TD\" unit=\"celsius\" value=\"13.9\"/></location></time><time datatype=\"forecast\" from=\"2019-05-21T19:00:00Z\" to=\"2019-05-21T20:00:00Z\"><location altitude=\"485\" latitude=\"60.1000\" longitude=\"9.5800\"><precipitation unit=\"mm\" value=\"0.9\" minvalue=\"0.5\" maxvalue=\"1.6\"/><symbol id=\"LightRain\" number=\"9\"/></location></time></product></weatherdata>"
// The first humidity in this response is different
const val badHumiditySampleResponse = "<weatherdata xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://api.met.no/weatherapi/locationforecast/1.9/schema\" created=\"2019-05-21T19:43:42Z\"><meta><model name=\"LOCAL\" termin=\"2019-05-21T12:00:00Z\" runended=\"2019-05-21T14:30:11Z\" nextrun=\"2019-05-21T22:00:00Z\" from=\"2019-05-21T20:00:00Z\" to=\"2019-05-24T06:00:00Z\"/><model name=\"EPS\" termin=\"2019-05-21T00:00:00Z\" runended=\"2019-05-21T09:04:59Z\" nextrun=\"2019-05-21T22:00:00Z\" from=\"2019-05-24T12:00:00Z\" to=\"2019-05-30T18:00:00Z\"/></meta><product class=\"pointData\"><time datatype=\"forecast\" from=\"2019-05-21T20:00:00Z\" to=\"2019-05-21T20:00:00Z\"><location altitude=\"485\" latitude=\"60.1000\" longitude=\"9.5800\"><temperature id=\"TTT\" unit=\"celsius\" value=\"13.9\"/><windDirection id=\"dd\" deg=\"36.5\" name=\"NE\"/><windSpeed id=\"ff\" mps=\"0.8\" beaufort=\"1\" name=\"Flau vind\"/><windGust id=\"ff_gust\" mps=\"4.9\"/><areaMaxWindSpeed mps=\"3.0\"/><humidity value=\"99999999\" unit=\"percent\"/><pressure id=\"pr\" unit=\"hPa\" value=\"1009.7\"/><cloudiness id=\"NN\" percent=\"100.0\"/><fog id=\"FOG\" percent=\"6.0\"/><lowClouds id=\"LOW\" percent=\"52.9\"/><mediumClouds id=\"MEDIUM\" percent=\"97.6\"/><highClouds id=\"HIGH\" percent=\"99.5\"/><dewpointTemperature id=\"TD\" unit=\"celsius\" value=\"13.9\"/></location></time><time datatype=\"forecast\" from=\"2019-05-21T19:00:00Z\" to=\"2019-05-21T20:00:00Z\"><location altitude=\"485\" latitude=\"60.1000\" longitude=\"9.5800\"><precipitation unit=\"mm\" value=\"0.9\" minvalue=\"0.5\" maxvalue=\"1.6\"/><symbol id=\"LightRain\" number=\"9\"/></location></time></product></weatherdata>"

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