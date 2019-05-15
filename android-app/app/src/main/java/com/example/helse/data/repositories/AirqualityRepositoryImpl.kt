package com.example.helse.data.repositories

import androidx.fragment.app.Fragment
import androidx.annotation.WorkerThread
import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.utilities.*

interface AirqualityRepository {
    suspend fun fetchAirquality(): MutableList<AirqualityForecast>
}

class AirqualityRepositoryImpl(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityApi,
    fragment: Fragment,
    val location: Location
) : AirqualityRepository {

    private val preferences: Preferences = Injector.getAppPreferences(AppContext.getAppContext())

    @WorkerThread
    override suspend fun fetchAirquality(): MutableList<AirqualityForecast> {
        lateinit var airquality: MutableList<AirqualityForecast>

        val timeNow = System.currentTimeMillis()
        val timePrev = preferences.getLastApiCall(location, LAST_API_CALL_AIRQUALITY)

        //if -1 there is no previous fetch call
        if (timePrev < 0 || (timeNow - timePrev) >= THIRTY_MINUTES) {
            airquality = airqualityApi.fetchAirquality()
            airqualityDao.insert(
                airquality
            )
            preferences.setLastApiCall(
                location, LAST_API_CALL_AIRQUALITY, timeNow
            )
        } else {
            airquality = airqualityDao.get(location.stationID)
        }
        return airquality
    }
}
