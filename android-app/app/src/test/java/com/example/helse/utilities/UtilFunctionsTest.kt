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

        assertEquals(calculateRiskFor(AirqualityMetrics.O3, 2.0), "")
    }

    @Test
    fun test_calculateRiskFor_with_low_value() {
        assertEquals(calculateRiskFor(AirqualityMetrics.O3, 0.0), "LAV RISIKO")
    }

    @Test
    fun test_calculateRiskFor_with_very_high_value() {
        assertEquals(calculateRiskFor(AirqualityMetrics.O3, 100.0), "LAV RISIKO")
    }

    @Test
    fun test_calculateOverallRiskValue_highest_value() {
        val riskValues = arrayOf("LAV RISIKO", "LAV RISIKO", "LAV RISIKO", "ALVORLIG RISIKO")
        assertEquals(calculateOverallRiskValue(riskValues), "ALVORLIG RISIKO")
    }

    @Test
    fun test_calculateOverallRiskValue_all_low_value() {
        val riskValues = arrayOf("LAV RISIKO", "LAV RISIKO", "LAV RISIKO", "LAV RISIKO")
        assertEquals(calculateOverallRiskValue(riskValues), "LAV RISIKO")
    }

    @Test
    fun test_convertRiskToInt_low_value() {
        assertEquals(convertRiskToInt("LAV RISIKO"), 2)
    }

    @Test
    fun test_convertRiskToInt_unexpected_value() {
        assertEquals(convertRiskToInt(""), 0)
    }

    @Test
    fun test_calculateUvRiskValue_low_value() {
        assertEquals(calculateUvRiskValue(0.0), "LAV RISIKO")
    }

    @Test
    fun test_calculateUvRiskValue_very_high_value() {
        assertEquals(calculateUvRiskValue(100.0), "ALVORLIG RISIKO")
    }

    @Test
    fun test_calculateHumidityRiskValue_low_value() {
        assertEquals(calculateHumidityRiskValue(0.0), "LAV FUKTIGHET")
    }

    @Test
    fun test_calculateHumidityRiskValue_very_high_value() {
        assertEquals(calculateHumidityRiskValue(100.0), "HØY FUKTIGHET")
    }

    @Test
    fun test_calculateDistanceBetweenCoordinates() {
        //The right answer is double checked with online calculator 4460.7 km ~
        assertEquals(calculateDistanceBetweenCoordinates(30.0, 80.0, 60.0, 120.0), 4460700.0, 300.00)
    }

}