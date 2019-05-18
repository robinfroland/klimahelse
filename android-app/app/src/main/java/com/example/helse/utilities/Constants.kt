package com.example.helse.utilities

import android.Manifest
import android.content.pm.PackageManager


// BASE URL
const val AIRQUALITY_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?"
const val HUMIDITY_BASE_URL = "https://api.met.no/weatherapi/locationforecast/1.9/?"
const val UV_BASE_URL = "https://api.met.no/weatherapi/uvforecast/1.0/available"
const val STATIONS_BASE_URL = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"

// PERMISSIONS
const val LOCATION_PERMISSION_CODE = 1
const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
const val PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED

// MODULE KEYS
const val AIRQUALITY_MODULE = "AIRQUALITY_MODULE"
const val UV_MODULE = "UV_MODULE"
const val HUMIDITY_MODULE = "HUMIDITY_MODULE"

// SHARED PREFERENCE KEYS
const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"
const val ENABLE_NOTIFICATIONS = "_ENABLE_NOTIFICATIONS"
const val ENABLE_MODULE = "_ENABLE_MODULE"
const val LAST_API_CALL_AIRQUALITY = "LAST_API_CALL_AIRQUALITY"
const val LAST_API_CALL_UV = "LAST_API_CALL_UV"
const val LAST_API_CALL_HUMIDTY = "LAST_API_CALL_HUMIDTY"
const val LOCATION_SETTINGS = "LOCATION_SETTINGS"


// MODULE RISK LABELS
const val LOW_HUMIDITY_VALUE = "LAV"
const val GOOD_HUMIDITY_VALUE = "PASSE"
const val HIGH_HUMIDITY_VALUE = "HØY"
const val LOW_VALUE = "LAV RISIKO"            //low       = <1.0-2.0>
const val MEDIUM_VALUE = "MODERAT RISIKO"     //medium    = <2.1-3.0>
const val HIGH_VALUE = "BETYDELIG RISIKO"     //high      = <3.1-4.0>
const val VERY_HIGH_VALUE = "ALVORLIG RISIKO" //very high = <4.1-5.0>

// MISC
const val SETTINGS_LABEL = "Innstillinger"

const val OFFSET_FOR_HORIZONTAL_SLIDER = -1
const val OFFSET_FOR_HORIZONTAL_SLIDER_CENTER = -2
const val ORIGINAL_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val DATE_PATTERN = "d MMMM HH:mm"

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

const val THIRTY_MINUTES = 30 * 60 * 1000
const val ONE_DAY = 1000 /*ms in second*/ * 60 /*Seconds in minute*/ * 60 /*Minutes in hour*/ * 24 /*Hours in day*/