package com.example.helse.utilities

import android.Manifest
import android.content.pm.PackageManager

const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"
const val LOCATION_PERMISSION_CODE = 1
const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
const val PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED

const val LOW_AQI_VALUE = 2.00      //low       = <1.0-2.0>
const val MEDIUM_AQI_VALUE= 3.00    //medium    = <2.1-3.0>
const val HIGH_AQI_VALUE = 4.00     //high      = <3.1-4.0>
const val VERY_HIGH_AQI_VALUE= 5.00 //very high = <4.1-5.0>



