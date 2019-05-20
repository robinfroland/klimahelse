package com.example.helse.data.repositories

import com.example.helse.data.api.AirqualityForecastApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.*

class AirqualityForecastRepository(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityForecastApi
) {
    private val preferences: Preferences = Injector.getAppPreferences(AppContext.getAppContext())

    // Default location is the location selected by the user
    fun fetchAirquality(): MutableList<AirqualityForecast> {
        val location = airqualityApi.location

        lateinit var airqualityForecast: MutableList<AirqualityForecast>

        if (dataIsStale(location)) {
            // If data is stale and api fetch is needed
            airqualityDao.delete(location.stationID)
            airqualityForecast = airqualityApi.fetchAirqualityFromURL()
            airqualityDao.insert(airqualityForecast)
            preferences.setLastApiCall(
                location, LAST_API_CALL_AIRQUALITY, System.currentTimeMillis()
            )
        } else {
            // Retrieve data from database
            airqualityForecast = airqualityDao.get(location.stationID)
            if (airqualityForecast.isEmpty()) return airqualityApi.fetchAirqualityFromURL()
        }
        if (airqualityForecast.size == 0) {
            return mutableListOf(emptyAirqualityForecast)
        }
        return airqualityForecast
    }

    private fun dataIsStale(location: Location): Boolean {
        val previousFetchTime = preferences.getLastApiCall(location, LAST_API_CALL_AIRQUALITY)
        val currentTime = System.currentTimeMillis()
        if (airqualityDao.get(location.stationID).size < 20) {
            return true
        }

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
