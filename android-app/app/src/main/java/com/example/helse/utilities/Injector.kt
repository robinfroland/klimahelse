package com.example.helse.utilities

import android.content.Context
import com.example.helse.data.api.AirqualityForecastApi
import com.example.helse.data.api.HumidityForecastApi
import com.example.helse.data.api.LocationApi
import com.example.helse.data.api.UvForecastApi
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.AirqualityForecastRepository
import com.example.helse.data.repositories.HumidityForecastRepository
import com.example.helse.data.repositories.LocationRepository
import com.example.helse.data.repositories.UvForecastRepository

/**
 * Injector for useful getters. Removes boilerplate code.
 */

object Injector {

    fun getAppPreferences(context: Context): AppPreferences {
        return AppPreferences.getInstance(context)
    }

    fun getLocation(context: Context): Location {
        return getAppPreferences(context).getLocation()
    }

    fun getLocationRepository(context: Context): LocationRepository {
        return LocationRepository.getInstance(
            LocalDatabase.getInstance(context).locationDao(), LocationApi
        )
    }

}