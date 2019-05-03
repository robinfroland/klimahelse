package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast
import java.util.*

interface AirqualityRepository {
    suspend fun fetchAirquality(): MutableList<AirqualityForecast>
}

class AirqualityRepositoryImpl(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityApi

) : AirqualityRepository {

    private var timeNow = Calendar.getInstance().get(Calendar.MINUTE)
    private var timePrev = timeNow


    @WorkerThread
    override suspend fun fetchAirquality(): MutableList<AirqualityForecast> {

        //if not any data fetched
        if(!alreadyFetched()) {
            //fetch and insert into database
            airqualityDao.insertAll(
                airqualityApi.fetchAirquality()
            )
            // reset check
            timePrev = timeNow

        //if data already fetched
        } else {
            //check time since it was last checked
            timeNow = Calendar.getInstance().get(Calendar.MINUTE)
            if(timeNow - timePrev >= 30){
                //if data was fetched over half hour ago fetch again
                airqualityDao.insertAll(
                    airqualityApi.fetchAirquality()
                )
            }
        }
        return airqualityDao.getAll()
    }

    private fun alreadyFetched(): Boolean {
        return airqualityDao.getAll().isNotEmpty()
    }
}