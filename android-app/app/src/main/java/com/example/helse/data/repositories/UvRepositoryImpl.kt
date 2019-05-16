package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import com.example.helse.data.api.UvApi
import com.example.helse.data.database.UVDao
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.entities.emptyLocation
import com.example.helse.utilities.*

interface UvRepository {
    suspend fun fetchUv(): MutableList<UvForecast>
}

class UvRepositoryImpl(
    private val uvDao: UVDao,
    private val uvApi: UvApi,
    fragment: Fragment,
    private val location: Location
) : UvRepository {
    private val preferences: Preferences = Injector.getAppPreferences(fragment.requireContext())

    @WorkerThread
    override suspend fun fetchUv(): MutableList<UvForecast> {
        lateinit var uvForecast: MutableList<UvForecast>

        val timeNow = System.currentTimeMillis()
        val timePrev = preferences.getLastApiCall(location, LAST_API_CALL_UV)

        //if -1 there is no previous fetch call
        if (timePrev < 0 || (timeNow - timePrev) >= ONE_DAY) {
            uvForecast = uvApi.fetchUv()
            preferences.setLastApiCall(emptyLocation, LAST_API_CALL_UV, timeNow)
            uvDao.insertAll(uvForecast)
        } else {
            uvForecast = uvDao.getAll()
        }
        return uvForecast
    }
}