package com.example.helse.data.repositories

import com.example.helse.data.api.RemoteForecastData
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.*

private const val AIRQUALITY_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?"

class AirqualityForecastRepository(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: RemoteForecastData<AirqualityForecast>,
    private val preferences: Preferences
) : ForecastRepository<AirqualityForecast> {

    // Default location is the location selected by the user
    override fun getForecast(location: Location): List<AirqualityForecast> {
        lateinit var airqualityForecast: List<AirqualityForecast>

        if (fetchIsNeeded(location)) {
            // If data is stale or never fetched and data retrieval from remote source is needed
            airqualityDao.delete(location.stationID)
            airqualityForecast = airqualityApi.fetchForecast(location)
            airqualityDao.insert(airqualityForecast)
            preferences.setLastApiCall(
                location, LAST_API_CALL_AIRQUALITY, System.currentTimeMillis()
            )
        } else {
            // Retrieve data from database
            airqualityForecast = airqualityDao.get(location.stationID)
            if (airqualityForecast.isEmpty()) return airqualityApi.fetchForecast(location)
        }
        if (airqualityForecast.isEmpty()) {
            return mutableListOf(emptyAirqualityForecast)
        }
        return airqualityForecast
    }

    override fun fetchIsNeeded(location: Location): Boolean {
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
            airqualityForecastApi: RemoteForecastData<AirqualityForecast>,
            preferences: Preferences
        ) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AirqualityForecastRepository(airqualityDao, airqualityForecastApi, preferences)
                    .also { INSTANCE = it }
            }
    }
}
