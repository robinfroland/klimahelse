package com.example.helse.data.repositories

import androidx.fragment.app.Fragment
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.utilities.Injector

interface AirqualityRepository {
    suspend fun fetchAirquality(): MutableList<AirqualityForecast>
}

class AirqualityRepositoryImpl(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityApi,
    fragment: Fragment

) : AirqualityRepository {
    private val TEN_MINUTES = 5 * 10 * 1000
    private var timeNow = System.currentTimeMillis() - TEN_MINUTES
    private var timePrev = timeNow
    private val preferences = Injector.getAppPreferences(fragment.requireContext())


    @WorkerThread
    override suspend fun fetchAirquality(): MutableList<AirqualityForecast> {

        //if not any data fetched
        if(!alreadyFetched()) {
            Log.i("ARAN", "Hentet first time fetch ")
            //fetch and insert into database
            airqualityDao.insertAll(
                airqualityApi.fetchAirquality()
            )
            // reset check

        //if data already fetched
        } else {
            Log.i("ARAN", "Data allerede hentet ")
            //check time since it was last checked
            timeNow = System.currentTimeMillis() - TEN_MINUTES
            if(timeNow > timePrev){
                Log.i("ARAN", "Det har gått 30")
                //if data was fetched over half hour ago fetch again
                airqualityDao.insertAll(
                    airqualityApi.fetchAirquality()
                )
            } else {
                val t = timeNow - timePrev
                Log.i("ARAN", "Det har ikke gått 30 time {$t}")
            }
        }
        return airqualityDao.getAll()
    }

    private fun alreadyFetched(): Boolean {
        return airqualityDao.getAll().isNotEmpty()
    }
}