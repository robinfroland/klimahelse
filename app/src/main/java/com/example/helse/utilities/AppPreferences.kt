package com.example.helse.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat
import com.example.helse.data.entities.Module

interface Preferences {
    fun setFirstLaunch(isFirst: Boolean)
    fun isFirstLaunch(): Boolean
    fun locationPermissionGranted(): Boolean
    fun enableNotifications(module: Module, enable: Boolean)
    fun isNotificationEnabled(module: Module): Boolean
    fun getSharedPreferences(): SharedPreferences
}

class AppPreferences(context: Context) : Preferences {


    private val applicationContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)

    private val preferenceEditor = preferences.edit()

    override fun getSharedPreferences(): SharedPreferences {
        return preferences
    }

    override fun isFirstLaunch(): Boolean {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true)
    }

    override fun setFirstLaunch(isFirst: Boolean) {
        preferenceEditor.putBoolean(IS_FIRST_LAUNCH, isFirst)
        preferenceEditor.apply()
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


