package com.example.helse.utilities

import android.Manifest
import android.content.pm.PackageManager

const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"
const val OFFSET_FOR_HORIZONTAL_SLIDER = -1
const val OFFSET_FOR_HORIZONTAL_SLIDER_CENTER = -2
const val LOCATION_PERMISSION_CODE = 1
const val ORIGINAL_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val DATE_PATTERN = "d MMMM HH:mm"
const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
const val PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED

const val LOW_AQI_VALUE = "LAV"            //low       = <1.0-2.0>
const val MEDIUM_AQI_VALUE = "MODERAT"     //medium    = <2.1-3.0>
const val HIGH_AQI_VALUE = "BETYDELIG"     //high      = <3.1-4.0>
const val VERY_HIGH_AQI_VALUE = "ALVORLIG" //very high = <4.1-5.0>

enum class AirqualityMetrics {
    O3,
    PM10,
    PM25,
    NO2
}

val airqualityMetricRanges = HashMap<AirqualityMetrics, Array<Int>>()

val o3Values = arrayOf(100, 180, 240)
val pm10Values = arrayOf(60, 120, 400)
val pm25Values = arrayOf(30, 50, 150)
val no2Values = arrayOf(100, 200, 400)
