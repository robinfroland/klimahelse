package com.example.helse.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"

class AppPreferences(context: Context) : Preferences {
    private val appContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    private val preferenceEditor = preferences.edit()

    override fun setFirstLaunch(isFirst: Boolean) {
        preferenceEditor.putBoolean(IS_FIRST_LAUNCH, isFirst)
        preferenceEditor.apply()
    }

    override fun isFirstLaunch(): Boolean {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true)
    }


}


interface Preferences {
    fun setFirstLaunch(isFirst: Boolean)
    fun isFirstLaunch(): Boolean


}