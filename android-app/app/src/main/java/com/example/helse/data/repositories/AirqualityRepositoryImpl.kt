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

        //if -1 there is no previous fetch call
        if (timePrev < 0) {
            fetchNew(timeNow)
        } else {
            //check if 30 min has passed since last fetch call
            if ((timeNow - timePrev) >= THIRTY_MINUTES) {
                fetchNew(timeNow)
            }
        }
        val dao = airqualityDao.get(location.stationID)
        println("Returning $dao")
        return dao
    }

    private fun fetchNew(timeNow: Long) {
        println("Fetching new")
        airqualityDao.insert(
            airqualityApi.fetchAirquality()
        )
        preferences.setLastApiCall(
            location, LAST_API_CALL_AIRQUALITY, timeNow
        )
    }
}
