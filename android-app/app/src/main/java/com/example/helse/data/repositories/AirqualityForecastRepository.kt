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
    private val savedLocation = preferences.getLocation()

    // Default location is the location selected by the user
    fun fetchAirquality(location: Location = savedLocation): MutableList<AirqualityForecast> {

        lateinit var airqualityForecast: MutableList<AirqualityForecast>

        println("location is $location")

        if (dataIsStale(location)) {
            // If data is stale and api fetch is needed
            println("Fetching")
            airqualityDao.delete(location.stationID)
            airqualityForecast = airqualityApi.fetchAirqualityFromURL()
            airqualityDao.insert(airqualityForecast)
            preferences.setLastApiCall(
                location, LAST_API_CALL_AIRQUALITY, System.currentTimeMillis()
            )
        } else {
            // Retrieve data from database
            println("Getting from database")
            airqualityForecast = airqualityDao.get(location.stationID)
            if (airqualityForecast.isEmpty()) return airqualityApi.fetchAirqualityFromURL()
        }
        if (airqualityForecast.size == 0) {
            return mutableListOf(emptyAirqualityForecast)
        }
        println("airqualityForecastRepo $airqualityForecast")
        return airqualityForecast
    }

    private fun dataIsStale(location: Location): Boolean {
        val previousFetchTime = preferences.getLastApiCall(location, LAST_API_CALL_AIRQUALITY)
        println("previousFetch time for ${location.stationID} was $previousFetchTime")
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
