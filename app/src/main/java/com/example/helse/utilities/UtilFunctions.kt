package com.example.helse.utilities

fun setupAirqualityRiskValues() {
    airqualityMetricRanges[AirqualityMetrics.NO2] = no2Values
    airqualityMetricRanges[AirqualityMetrics.PM10] = pm10Values
    airqualityMetricRanges[AirqualityMetrics.PM25] = pm25Values
    airqualityMetricRanges[AirqualityMetrics.O3] = o3Values
}

fun calculateRiskFor(metric: AirqualityMetrics, value: Double): String {
    if (airqualityMetricRanges.size != 4) {
        setupAirqualityRiskValues()
    }

    val values = airqualityMetricRanges[metric]
    if (values == null || values.size != 3) {
        println("Something went pretty wrong lol, you fucked up")
        return ""
    }
    val low = values[0]
    val medium = values[1]
    val high = values[2]

    return when {
        value <= low -> LOW_AQI_VALUE
        value <= medium -> MEDIUM_AQI_VALUE
        value <= high -> HIGH_AQI_VALUE
        else -> VERY_HIGH_AQI_VALUE
    }
}

fun calculateOverallRiskValue(
    riskValues: Array<String>
): String {
    var highestRisk = 0
    riskValues.forEach { riskValue ->
        val risk = convertRiskToInt(riskValue)

        if (risk > highestRisk) {
            highestRisk = risk
        }
    }

    return when {
        highestRisk <= 2 -> LOW_AQI_VALUE
        highestRisk <= 3 -> MEDIUM_AQI_VALUE
        highestRisk <= 4 -> HIGH_AQI_VALUE
        else -> VERY_HIGH_AQI_VALUE
    }
}

fun convertRiskToInt(riskString: String): Int {
    return when (riskString) {
        LOW_AQI_VALUE -> 2
        MEDIUM_AQI_VALUE -> 3
        HIGH_AQI_VALUE -> 4
        VERY_HIGH_AQI_VALUE -> 5
        // Something went wrong, don't count it
        else -> 0
        // Divide by 4 to get average
    }
}
