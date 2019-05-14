package com.example.helse.data.repositories

import androidx.fragment.app.Fragment
import androidx.annotation.WorkerThread
import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyAirqualityForecast
import com.example.helse.utilities.Injector
import com.example.helse.utilities.LAST_API_CALL_AIRQUALITY
import com.example.helse.utilities.THIRTY_MINUTES
import com.example.helse.utilities.Preferences
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Error
import java.util.HashMap

interface AirqualityRepository {
    suspend fun fetchAirquality(): MutableList<AirqualityForecast>
}

class AirqualityRepositoryImpl(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityApi,
    fragment: Fragment,
    val location: Location
) : AirqualityRepository {
    private val preferences: Preferences = Injector.getAppPreferences(fragment.requireContext())

    @WorkerThread
    override suspend fun fetchAirquality(): MutableList<AirqualityForecast> {
        val timeNow = System.currentTimeMillis()
        val timePrev = preferences.getLastApiCall(location, LAST_API_CALL_AIRQUALITY)

        return getAirqualityAsync(airqualityApi, timeNow, timePrev).await()
    }

    private fun getAirqualityAsync(
        airqualityApi: AirqualityApi,
        timeNow: Long,
        timePrev: Long
    ): Deferred<MutableList<AirqualityForecast>> {

        return GlobalScope.async {
            lateinit var airquality: MutableList<AirqualityForecast>

            //if -1 there is no previous fetch call
            if (timePrev < 0 || (timeNow - timePrev) >= THIRTY_MINUTES) {
                println("Fetching new")
                airquality = airqualityApi.fetchAirquality()
                airqualityDao.insert(
                    airquality
                )
            } else {
                println("Getting from database")
                airquality = airqualityDao.get(location.stationID)
            }
            preferences.setLastApiCall(
                location, LAST_API_CALL_AIRQUALITY, timeNow
            )
            airquality
        }
    }
}
