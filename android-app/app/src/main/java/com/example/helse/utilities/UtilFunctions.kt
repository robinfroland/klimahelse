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
        value <= low -> LOW_VALUE
        value <= medium -> MEDIUM_VALUE
        value <= high -> HIGH_VALUE
        else -> VERY_HIGH_VALUE
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
        highestRisk <= 2 -> LOW_VALUE
        highestRisk <= 3 -> MEDIUM_VALUE
        highestRisk <= 4 -> HIGH_VALUE
        else -> VERY_HIGH_VALUE
    }
}

fun convertRiskToInt(riskString: String): Int {
    return when (riskString) {
        LOW_VALUE -> 2
        MEDIUM_VALUE -> 3
        HIGH_VALUE -> 4
        VERY_HIGH_VALUE -> 5
        // Something went wrong, don't count it
        else -> 0
        // Divide by 4 to get average
    }
}

fun calculateUvRiskValue(uvi_val: Double): String {
    return when {
        uvi_val <= 2.9 -> LOW_VALUE
        uvi_val <= 5.9 -> MEDIUM_VALUE
        uvi_val <= 7.9 -> HIGH_VALUE
        else -> VERY_HIGH_VALUE
    }
}

fun calculateHumidityRiskValue(humidity_val: Double): String {
    return when {
        humidity_val <= 30 -> LOW_HUMIDITY_VALUE
        humidity_val <= 60 -> GOOD_HUMIDITY_VALUE
        else -> HIGH_HUMIDITY_VALUE
    }
}


fun calculateDistanceBetweenCoordinates(fromLat: Double, fromLong: Double, toLat: Double, toLong: Double): Double {
    val R = 6371e3
    val lat1Radians = Math.toRadians(fromLat)
    val lat2Radians = Math.toRadians(toLat)
    val deltaLat = Math.toRadians(toLat - fromLat)
    val deltaLon = Math.toRadians(toLong - fromLong)

    val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
            Math.cos(lat1Radians) * Math.cos(lat2Radians) *
            Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return R * c
}
