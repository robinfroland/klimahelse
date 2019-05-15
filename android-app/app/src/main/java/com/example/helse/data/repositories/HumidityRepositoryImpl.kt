package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import com.example.helse.data.api.HumidityApi
import com.example.helse.data.database.HumidityDao
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.Location
import com.example.helse.utilities.*

interface HumidityRepository {
    suspend fun fetchHumidity(): MutableList<HumidityForecast>
}

class HumidityRepositoryImpl(
    private val humidityDao: HumidityDao,
    private val humidityApi: HumidityApi
) : HumidityRepository {

    private val preferences: Preferences = Injector.getAppPreferences(AppContext.getAppContext())
    private val location: Location = preferences.getLocation()

    @WorkerThread
    override suspend fun fetchHumidity(): MutableList<HumidityForecast> {
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
}