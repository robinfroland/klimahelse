package com.example.helse.data.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import com.example.helse.data.api.HumidityApi
import com.example.helse.data.database.HumidityDao
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.utilities.Injector
import com.example.helse.utilities.LAST_API_CALL_HUMIDTY
import com.example.helse.utilities.THIRTY_MINUTES
import com.example.helse.utilities.Preferences

interface HumidityRepository {
    suspend fun fetchHumidity(): MutableList<HumidityForecast>
}

class HumidityRepositoryImpl(
    private val humidityDao: HumidityDao,
    private val humidityApi : HumidityApi,
    fragment: Fragment
) : HumidityRepository {
    private val preferences: Preferences = Injector.getAppPreferences(fragment.requireContext())

    @WorkerThread
    override suspend fun fetchHumidity(): MutableList<HumidityForecast> {
        val timeNow = System.currentTimeMillis()
        val timePrev = preferences.getLastApiCall(LAST_API_CALL_HUMIDTY)

        //if -1 there is no previous fetch call
        if(timePrev < 0){
            fetchNew(timeNow)
        } else {
            //check if 30 min has passed since last fetch call
            if((timeNow - timePrev) >= THIRTY_MINUTES){
                fetchNew(timeNow)
            }
        }
        return humidityDao.getAll()
    }

    private fun fetchNew(timeNow: Long) {
        humidityDao.insertAll(
            humidityApi.fetchHumidity()
        )
        preferences.setLastApiCall(timeNow, LAST_API_CALL_HUMIDTY)
    }
}