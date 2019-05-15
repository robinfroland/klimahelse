package com.example.helse.data.repositories

import com.example.helse.data.api.AirqualityForecastApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.utilities.*

class AirqualityForecastRepository(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityForecastApi
) {

    private val preferences: Preferences = Injector.getAppPreferences(AppContext.getAppContext())
    private val location: Location = preferences.getLocation()

    fun fetchAirquality(): MutableList<AirqualityForecast> {
        lateinit var airqualityForecast: MutableList<AirqualityForecast>

        if (dataIsStale()) {
            // If data is stale and api fetch is needed
            airqualityForecast = airqualityApi.fetchAirquality()
            airqualityDao.insert(airqualityForecast)
            preferences.setLastApiCall(
                location, LAST_API_CALL_AIRQUALITY, System.currentTimeMillis()
            )
        } else {
            // Retrieve data from database
            airqualityForecast = airqualityDao.get(location.stationID)
        }
        return airqualityForecast
    }

    private fun dataIsStale(): Boolean {
        val previousFetchTime = preferences.getLastApiCall(location, LAST_API_CALL_AIRQUALITY)
        val currentTime = System.currentTimeMillis()

        // If -1 there is no previous fetch call, thus fetch is needed
        return previousFetchTime < 0 || (currentTime - previousFetchTime) >= THIRTY_MINUTES
    }

    companion object {

        private var INSTANCE: AirqualityForecastRepository? = null

        // Singleton instantiation of repository

        fun getInstance(
            airqualityDao: AirqualityDao,
            airqualityForecastApi: AirqualityForecastApi
        ) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AirqualityForecastRepository(airqualityDao, airqualityForecastApi)
                    .also { INSTANCE = it }
            }
    }
}
