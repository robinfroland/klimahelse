package com.example.helse.utilities

import android.content.Context

object Injector {

    fun getAppPreferences(context: Context): AppPreferences {
        return AppPreferences.getInstance(context)
    }
}