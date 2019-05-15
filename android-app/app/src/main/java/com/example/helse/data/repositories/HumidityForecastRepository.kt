package com.example.helse.data.repositories

import com.example.helse.data.api.HumidityForecastApi
import com.example.helse.data.database.HumidityDao
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.Location
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
            // If data is stale and api fetch is needed
            humidityForecast = humidityApi.fetchHumidity()
            humidityDao.insertAll(humidityForecast)
            preferences.setLastApiCall(
                location, LAST_API_CALL_HUMIDTY, System.currentTimeMillis()
            )
        } else {
            // Retrieve data from database
            humidityForecast = humidityDao.getAll()
        }
        return humidityForecast
    }

    private fun dataIsStale(): Boolean {
        val previousFetchTime = preferences.getLastApiCall(location, LAST_API_CALL_AIRQUALITY)
        val currentTime = System.currentTimeMillis()

        // If -1 there is no previous fetch call, thus fetch is needed
        return previousFetchTime < 0 || (currentTime - previousFetchTime) >= THIRTY_MINUTES
    }

    // Singleton instantiation
    companion object {
        @Volatile private var instance: HumidityForecastRepository? = null

        fun getInstance(humidityDao: HumidityDao, humidityApi: HumidityForecastApi) =
            instance ?: synchronized(this) {
                instance ?: HumidityForecastRepository(humidityDao, humidityApi).also { instance = it }
            }
    }
}