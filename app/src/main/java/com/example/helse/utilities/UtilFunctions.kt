package com.example.helse.utilities

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> List<*>.itemsAreOfType() =
    if (all { it is T })
        this as ArrayList<T>
    else null

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

fun calculateOverallRiskValue(x: Double): String {
    return when {
        x < 2 -> LOW_AQI_VALUE
        x < 3 -> MEDIUM_AQI_VALUE
        x < 4 -> HIGH_AQI_VALUE
        else -> VERY_HIGH_AQI_VALUE
    }
}
