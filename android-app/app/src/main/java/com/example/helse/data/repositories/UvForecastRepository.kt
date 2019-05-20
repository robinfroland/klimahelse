package com.example.helse.data.repositories

import com.example.helse.data.api.UvForecastApi
import com.example.helse.data.database.UVDao
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.entities.emptyLocation
import com.example.helse.data.entities.emptyUvForecast
import com.example.helse.utilities.*

class UvForecastRepository(
    private val uvDao: UVDao,
    private val uvApi: UvForecastApi
) {

    private val preferences: Preferences = Injector.getAppPreferences(AppContext.getAppContext())
    private val location: Location = preferences.getLocation()

    fun fetchUv(): MutableList<UvForecast> {
        lateinit var uvForecast: MutableList<UvForecast>

        if (dataIsStale()) {
            // If data is stale and api fetch is needed
            uvDao.deleteAll()
            uvForecast = uvApi.fetchUv()
            uvDao.insertAll(uvForecast)
            preferences.setLastApiCall(
                emptyLocation, LAST_API_CALL_UV, System.currentTimeMillis()
            )
        } else {
            // Retrieve data from database
            uvForecast = uvDao.getAll()
            var previousDistance = Double.MAX_VALUE
            var closestForecast = emptyUvForecast
            for (i in 0 until uvForecast.size) {
                val forecast = uvForecast[i]
                val distance = calculateDistanceBetweenCoordinates(
                    location.latitude,
                    location.longitude,
                    forecast.latitude,
                    forecast.longitude
                )

                if (distance < previousDistance) {
                    closestForecast = forecast
                    previousDistance = distance
                }
            }
            uvForecast = mutableListOf(closestForecast)
        }
        return uvForecast
    }

    private fun dataIsStale(): Boolean {
        val previousFetchTime = preferences.getLastApiCall(emptyLocation, LAST_API_CALL_UV)
        val currentTime = System.currentTimeMillis()
        if (uvDao.getAll().size < 10) {
            return true
        }
        // If -1 there is no previous fetch call, thus fetch is needed
        return previousFetchTime < 0 || (currentTime - previousFetchTime) >= THIRTY_MINUTES
    }

    companion object {
        private var INSTANCE: UvForecastRepository? = null

        // Singleton instantiation of repository
        fun getInstance(
            uvDao: UVDao,
            uvForecastApi: UvForecastApi
        ) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UvForecastRepository(uvDao, uvForecastApi)
                    .also { INSTANCE = it }
            }
    }
}