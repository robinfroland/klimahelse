package com.example.helse.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.Module

interface Preferences {
    fun setFirstLaunch(isFirst: Boolean)
    fun isFirstLaunch(): Boolean
    fun locationPermissionGranted(): Boolean
    fun enableNotifications(module: Module, enable: Boolean)
    fun isNotificationEnabled(module: Module): Boolean
    fun isModuleEnabled(module: Module): Boolean
    fun getSharedPreferences(): SharedPreferences
    fun setLocation(location: String, superlocation: String, lat: Double, lon: Double, stationID: String)
    fun getLocation(): Location
    fun setLastApiCall(location: Location, module: String, time: Long)
    fun getLastApiCall(location: Location, module: String): Long
}

class AppPreferences(context: Context) : Preferences {
    private val applicationContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)

    private val preferenceEditor = preferences.edit()

    override fun getSharedPreferences(): SharedPreferences {
        return preferences
    }

    private fun createKeyFor(location: Location, module: String): String {
        return "${location.latitude};${location.longitude};$module"
    }

    // The structure is meant to copy the structure of data class Location
    override fun setLocation(location: String, superlocation: String, lat: Double, lon: Double, stationID: String) {
        preferenceEditor.putString("CURRENT_LOCATION", "$location;$superlocation;$lat;$lon;$stationID")
        preferenceEditor.apply()
    }

    override fun getLocation(): Location {
        // Could return location directly, the following code is intended for ease of reading
        // What I want to highlight is what is returned at what position in the array
        val savedLocation =
            (preferences.getString("CURRENT_LOCATION", "Forskningsparken;Oslo;50.75;9.75;NO0057A")
                ?: "Forskningsparken;Oslo;50.75;9.75;NO0057A").split(";")
        val location = savedLocation[0]
        val superlocation = savedLocation[1]
        val lat = savedLocation[2]
        val lon = savedLocation[3]
        val stationID = savedLocation[4]

        return Location(location, superlocation, lat.toDouble(), lon.toDouble(), stationID)
    }

    override fun isFirstLaunch(): Boolean {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true)
    }

    override fun setFirstLaunch(isFirst: Boolean) {
        preferenceEditor.putBoolean(IS_FIRST_LAUNCH, isFirst)
        preferenceEditor.apply()
    }


    override fun setLastApiCall(location: Location, module: String, time: Long) {
        preferenceEditor.putLong(createKeyFor(location, module), time)
        preferenceEditor.apply()
    }

    override fun getLastApiCall(location: Location, module: String): Long {
        return preferences.getLong(createKeyFor(location, module), -1)

    }

    override fun enableNotifications(module: Module, enable: Boolean) {
        val key = module.moduleKey + ENABLE_NOTIFICATIONS
        preferenceEditor.putBoolean(key, enable)
        preferenceEditor.apply()
    }

    override fun isNotificationEnabled(module: Module): Boolean {
        val key = module.moduleKey + ENABLE_NOTIFICATIONS
        return preferences.getBoolean(key, false)
    }

    override fun isModuleEnabled(module: Module): Boolean {
        val key = module.moduleKey + ENABLE_MODULE
        return preferences.getBoolean(key, false)
    }

    override fun locationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            LOCATION_PERMISSION
        ) == PERMISSION_GRANTED
    }

    companion object {

        fun getInstance(context: Context): AppPreferences {
            val instance: AppPreferences? = null
            return instance ?: AppPreferences(context)

        }
    }
}


