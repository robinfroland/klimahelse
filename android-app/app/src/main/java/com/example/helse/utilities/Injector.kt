package com.example.helse.utilities

import android.content.Context
import com.example.helse.data.api.*
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.repositories.*

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

    fun getAirqualityForecastRepository(context: Context): ForecastRepository<AirqualityForecast> {
        return AirqualityForecastRepository(
            LocalDatabase.getInstance(context).airqualityDao(),
            AirqualityForecastApi(),
            getAppPreferences(context)
        )
    }

    fun getHumidityForecastRepository(context: Context): ForecastRepository<HumidityForecast> {
        return HumidityForecastRepository(
            LocalDatabase.getInstance(context).humidityDao(),
            HumidityForecastApi(),
            getAppPreferences(context)
        )
    }

    fun getUvForecastRepository(context: Context): ForecastRepository<UvForecast> {
        return UvForecastRepository(
            LocalDatabase.getInstance(context).uvDao(),
            UvForecastApi(),
            getAppPreferences(context)
        )
    }
}