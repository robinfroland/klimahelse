package com.example.helse.data.repositories

import com.example.helse.data.api.RemoteForecastData
import com.example.helse.data.database.UVDao
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.entities.emptyLocation
import com.example.helse.data.entities.emptyUvForecast
import com.example.helse.utilities.*

class UvForecastRepository(
    private val uvDao: UVDao,
    private val uvApi: RemoteForecastData<UvForecast>,
    private val preferences: Preferences
) : ForecastRepository<UvForecast> {

    override fun getForecast(location: Location): List<UvForecast> {
        lateinit var uvForecast: List<UvForecast>

        if (fetchIsNeeded(location)) {
            // If data is stale or never fetched and data retrieval from remote source is needed
            uvDao.deleteAll()
            uvForecast = uvApi.fetchForecast(location)
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

    override fun fetchIsNeeded(location: Location): Boolean {
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
            uvForecastApi: RemoteForecastData<UvForecast>,
            preferences: Preferences
        ) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UvForecastRepository(uvDao, uvForecastApi, preferences)
                    .also { INSTANCE = it }
            }
    }
}