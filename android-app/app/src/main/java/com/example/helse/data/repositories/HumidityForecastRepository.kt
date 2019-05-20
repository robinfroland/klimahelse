package com.example.helse.data.repositories

import com.example.helse.data.api.HumidityForecastApi
import com.example.helse.data.database.HumidityDao
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyHumidityForecast
import com.example.helse.utilities.*

class HumidityForecastRepository(
    private val humidityDao: HumidityDao,
    private val humidityApi: HumidityForecastApi
) {

    private val preferences: Preferences = Injector.getAppPreferences(AppContext.getAppContext())
    private val location: Location = preferences.getLocation()

    fun fetchHumidity(): MutableList<HumidityForecast> {
        lateinit var humidityForecast: MutableList<HumidityForecast>

        if (dataIsStale()) {
            // If data is stale an api fetch is needed
            humidityDao.deleteAll()
            humidityForecast = humidityApi.fetchHumidity()
            humidityDao.insert(humidityForecast)
            preferences.setLastApiCall(
                location, LAST_API_CALL_HUMIDTY, System.currentTimeMillis()
            )
        } else {
            // Retrieve data from database
            humidityForecast = humidityDao.getAll()
        }
        if (humidityForecast.size <= 2) {
            return mutableListOf(emptyHumidityForecast, emptyHumidityForecast)
        }
        return humidityForecast
    }

    private fun dataIsStale(): Boolean {
        val previousFetchTime = preferences.getLastApiCall(location, LAST_API_CALL_AIRQUALITY)
        val currentTime = System.currentTimeMillis()
        if (humidityDao.getAll().size < 1) {
            return true
        }

        // If -1 there is no previous fetch call, thus fetch is needed
        return previousFetchTime < 0 || (currentTime - previousFetchTime) >= THIRTY_MINUTES
    }

    companion object {
        private var INSTANCE: HumidityForecastRepository? = null

        // Singleton instantiation of repository
        fun getInstance(
            humidityDao: HumidityDao,
            humidityForecastApi: HumidityForecastApi
        ) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HumidityForecastRepository(humidityDao, humidityForecastApi)
                    .also { INSTANCE = it }
            }
    }
}