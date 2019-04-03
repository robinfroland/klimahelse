package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.emptyAirqualityForecast

interface AirqualityRepository {
    suspend fun fetchAirquality(): MutableList<AirqualityForecast>
}

class AirqualityRepositoryImpl(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityApi
) : AirqualityRepository {

    @WorkerThread
    override suspend fun fetchAirquality(): MutableList<AirqualityForecast> {

        /*val responseArray = airqualityApi.fetchAirquality()
        //if api call fails get from database and we have backup data return from database
        if(responseArray[0] == emptyAirqualityForecast && haveRecentData()) return airqualityDao.getAll()
        //return the response
        return responseArray*/
        return airqualityApi.fetchAirquality()
    }

    private fun haveRecentData() : Boolean {
        val recentAirqualityForecast = airqualityDao.getAll()
        if(recentAirqualityForecast.isEmpty()){
            return false
        }
        return true
    }
}