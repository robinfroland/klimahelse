package com.example.helse.data.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import com.example.helse.data.api.UvApi
import com.example.helse.data.database.UVDao
import com.example.helse.data.entities.UvForecast
import com.example.helse.utilities.Injector
import com.example.helse.utilities.LAST_API_CALL_UV
import com.example.helse.utilities.Preferences
import com.example.helse.utilities.THIRTY_MINUTES

interface UvRepository {
    suspend fun fetchUv(): MutableList<UvForecast>
}

class UvRepositoryImpl(
    private val uvDao: UVDao,
    private val uvApi: UvApi,
    fragment: Fragment
) : UvRepository {
    private val preferences: Preferences = Injector.getAppPreferences(fragment.requireContext())

    @WorkerThread
    override suspend fun fetchUv(): MutableList<UvForecast> {

        val timeNow = System.currentTimeMillis()
        val timePrev = preferences.getLastApiCall(LAST_API_CALL_UV)

        //if -1 there is no previous fetch call
        if (timePrev < 0) {
            Log.i("ARAN", "First time fetch")
            fetchNew(timeNow)
        } else {
            //check if 30 min has passed since last fetch call
            if ((timeNow - timePrev) >= THIRTY_MINUTES) {
                Log.i("ARAN", "10 seconds passed")
                fetchNew(timeNow)
            }
        }
        return uvDao.getAll()
    }

    private fun fetchNew(timeNow: Long) {
        uvDao.insertAll(
            uvApi.fetchUv()
        )
        preferences.setLastApiCall(timeNow, LAST_API_CALL_UV)
    }
}