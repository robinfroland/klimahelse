package com.example.helse.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface Preferences {
    fun setFirstLaunch(isFirst: Boolean)
    fun isFirstLaunch(): Boolean
}

const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"
const val LOCATION_PERMISSION_CODE = 1
const val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION

class AppPreferences(context: Context) : Preferences {

    private val applicationContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)

    private val preferenceEditor = preferences.edit()

    override fun isFirstLaunch(): Boolean {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true)
    }

    override fun setFirstLaunch(isFirst: Boolean) {
        preferenceEditor.putBoolean(IS_FIRST_LAUNCH, isFirst)
        preferenceEditor.apply()
    }

    private fun requestUserPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(LOCATION_PERMISSION),
            LOCATION_PERMISSION_CODE
        )
    }

    private fun hasUserPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            LOCATION_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Override in activity?
    private fun permissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // set location and save preference
                } else {
                    // save preference
                    // set switch false
                }
            }
        }
    }

}


