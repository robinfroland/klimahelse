package com.example.helse.utilities

import org.junit.Test

import org.junit.Assert.*


class UtilFunctionsTest {

    @Test
    fun test_calculateRiskFor_with_shorter_array_metric() {
        airqualityMetricRanges[AirqualityMetrics.NO2] = no2Values
        airqualityMetricRanges[AirqualityMetrics.PM10] = pm10Values
        airqualityMetricRanges[AirqualityMetrics.PM25] = pm25Values
        airqualityMetricRanges[AirqualityMetrics.O3] = arrayOf(1 , 2)

        assertEquals("",calculateRiskFor(AirqualityMetrics.O3, 2.0))
    }

    @Test
    fun test_calculateRiskFor_with_low_value() {
        assertEquals("LAV RISIKO",calculateRiskFor(AirqualityMetrics.O3, 0.0))
    }

    @Test
    fun test_calculateRiskFor_with_very_high_value() {
        assertEquals("LAV RISIKO",calculateRiskFor(AirqualityMetrics.O3, 100.0))
    }

    @Test
    fun test_calculateOverallRiskValue_highest_value() {
        val riskValues = arrayOf("LAV RISIKO", "LAV RISIKO", "LAV RISIKO", "ALVORLIG RISIKO")
        assertEquals( "ALVORLIG RISIKO", calculateOverallRiskValue(riskValues))
    }

    @Test
    fun test_calculateOverallRiskValue_invalid_values() {
        val riskValues = arrayOf("LAV", "RISIKO", "HALLOAN", "HMMM")
        assertEquals("", calculateOverallRiskValue(riskValues))
    }

    @Test
    fun test_calculateOverallRiskValue_all_low_value() {
        val riskValues = arrayOf("LAV RISIKO", "LAV RISIKO", "LAV RISIKO", "LAV RISIKO")
        assertEquals("LAV RISIKO", calculateOverallRiskValue(riskValues))
    }

    @Test
    fun test_convertRiskToInt_low_value() {
        assertEquals(2, convertRiskToInt("LAV RISIKO"))
    }

    @Test
    fun test_convertRiskToInt_unexpected_riskstring() {
        assertEquals(0, convertRiskToInt("HALLOAN"))
    }

    @Test
    fun test_convertRiskToInt_empty_riskString() {
        assertEquals(0, convertRiskToInt(""))
    }


    @Test
    fun test_calculateUvRiskValue_low_value() {
        assertEquals("LAV RISIKO", calculateUvRiskValue(0.0))
    }

    @Test
    fun test_calculateUvRiskValue_very_high_value() {
        assertEquals("ALVORLIG RISIKO", calculateUvRiskValue(100.0))
    }

    @Test
    fun test_calculateHumidityRiskValue_low_value() {
        assertEquals("LAV FUKTIGHET", calculateHumidityRiskValue(0.0))
    }

    @Test
    fun test_calculateHumidityRiskValue_very_high_value() {
        assertEquals("HØY FUKTIGHET", calculateHumidityRiskValue(100.0))
    }

    @Test
    fun test_calculateHumidityRiskValue_with_negative_value_return_low() {
        assertEquals("LAV FUKTIGHET", calculateHumidityRiskValue(-100.0))
    }

    @Test
    fun test_calculateDistanceBetweenCoordinates() {
        //The right answer is double checked with online calculator 4460.7 km ~
        assertEquals(4460700.0, calculateDistanceBetweenCoordinates(30.0, 80.0, 60.0, 120.0) ,300.00)
    }

    @Test
    fun test_calculateDistanceWithSameCoordinatesReturnsZeroDistance() {
        //The right answer is double checked with online calculator 4460.7 km ~
        assertEquals(0.00, calculateDistanceBetweenCoordinates(0.0, 0.0, 0.0, 0.0), 300.00)
    }
}