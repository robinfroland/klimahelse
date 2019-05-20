package com.example.helse.utilities

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.Module
import java.text.DecimalFormat
import java.util.*

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
    fun useDeviceLocation(): Boolean
    fun setDeviceLocation(lat: Double, lon: Double)
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

    private val defaultPreferenceLocation = "Alnabru;Oslo;59.92767;10.84655;NO0057A"
    // The structure is meant to copy the structure of data class Location
    override fun setLocation(location: String, superlocation: String, lat: Double, lon: Double, stationID: String) {
        val southernMostLatNorway = 57.711267
        val southernMostLonNorway = 4.810990
        val northernMostLatNorway = 70.996689
        val northernMostLonNorway = 33.48198
        if (lat < southernMostLatNorway ||
            lat > northernMostLatNorway ||
            lon < southernMostLonNorway ||
            lon > northernMostLonNorway
        ) {
            preferenceEditor.putString("CURRENT_LOCATION", defaultPreferenceLocation)
        } else {
            preferenceEditor.putString("CURRENT_LOCATION", "$location;$superlocation;$lat;$lon;$stationID")
        }
        preferenceEditor.apply()
    }

    override fun getLocation(): Location {
        // Could return location directly, the following code is intended for ease of reading
        // What I want to highlight is what is returned at what position in the array
        val savedLocation =
            (preferences.getString("CURRENT_LOCATION", defaultPreferenceLocation)
                ?: defaultPreferenceLocation).split(";")
        val location = savedLocation[0]
        val superlocation = savedLocation[1]
        val lat = savedLocation[2].toDouble()
        val lon = savedLocation[3].toDouble()
        val stationID = savedLocation[4]

        // Trunctate to 3 decimals to avoid unnecessary accuracy when fetching data later
        val coordinateTrunctation = DecimalFormat("#.###")
        val latitude = coordinateTrunctation.format(lat).toDouble()
        val longtitude = coordinateTrunctation.format(lon).toDouble()

        return Location(location, superlocation, latitude, longtitude, stationID)
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

    private fun getDeviceLocationObj(lat: Double, lon: Double): Location {
        val deviceLocation = Geocoder(applicationContext, Locale.getDefault())
        val geoLocation = deviceLocation.getFromLocation(lat, lon, 1)

        val superlocation = geoLocation[0].adminArea // e.g: Oslo
        val location = when {
            geoLocation[0].subLocality != null -> // e.g: Blindern
                geoLocation[0].subLocality
            geoLocation[0].premises != null -> // e.g: Kristen Nygaards hus
                geoLocation[0].premises
            else ->
                geoLocation[0].subAdminArea // e.g: Oslo kommune
        }
        

        return Location(location, superlocation, lat, lon, USE_DEVICE_LOCATION)
    }

    override fun setDeviceLocation(lat: Double, lon: Double) {
        val deviceLocation = getDeviceLocationObj(lat, lon)
        setLocation(
            deviceLocation.location,
            deviceLocation.superlocation,
            deviceLocation.latitude,
            deviceLocation.longitude,
            deviceLocation.stationID
        )
    }

    override fun useDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, false)
    }

    companion object {

        fun getInstance(context: Context): AppPreferences {
            val instance: AppPreferences? = null
            return instance ?: AppPreferences(context)

        }
    }
}


