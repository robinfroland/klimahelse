package com.example.helse.data.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import com.example.helse.data.api.HumidityApi
import com.example.helse.data.database.HumidityDao
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.Location
import com.example.helse.utilities.Injector
import com.example.helse.utilities.LAST_API_CALL_HUMIDTY
import com.example.helse.utilities.THIRTY_MINUTES
import com.example.helse.utilities.Preferences

interface HumidityRepository {
    suspend fun fetchHumidity(): MutableList<HumidityForecast>
}

class HumidityRepositoryImpl(
    private val humidityDao: HumidityDao,
    private val humidityApi: HumidityApi,
    fragment: Fragment,
    val location: Location
) : HumidityRepository {
    private val preferences: Preferences = Injector.getAppPreferences(fragment.requireContext())

    @WorkerThread
    override suspend fun fetchHumidity(): MutableList<HumidityForecast> {
        lateinit var humidity: MutableList<HumidityForecast>

        val timeNow = System.currentTimeMillis()
        val timePrev = preferences.getLastApiCall(location, LAST_API_CALL_HUMIDTY)

        // Data is either never fetched, or old, fetch new from API
        if (timePrev < 0 || (timeNow - timePrev) >= THIRTY_MINUTES) {
            humidity = humidityApi.fetchHumidity()
            humidityDao.insert(humidity)
            preferences.setLastApiCall(location, LAST_API_CALL_HUMIDTY, timeNow)
        } else {
            // Data exists in database, retrieve it
            humidity = humidityDao.getAll()
        }
        return humidity
    }
}