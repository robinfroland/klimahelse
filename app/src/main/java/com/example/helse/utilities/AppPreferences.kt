package com.example.helse.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat

interface Preferences {
    fun setFirstLaunch(isFirst: Boolean)
    fun isFirstLaunch(): Boolean
    fun locationPermissionGranted(): Boolean
}

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

    override fun locationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            LOCATION_PERMISSION
        ) == PERMISSION_GRANTED
    }

}


