package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast

interface AirqualityRepository {
    suspend fun fetchAirquality(): MutableList<AirqualityForecast>
}

class AirqualityRepositoryImpl(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityApi
) : AirqualityRepository {

    @WorkerThread
    override suspend fun fetchAirquality(): MutableList<AirqualityForecast> {
        if(!haveRecentData()) {
            airqualityDao.insertAll(
                airqualityApi.fetchAirquality()
            )
        }
        return airqualityDao.getAll()
    }

    private fun haveRecentData() : Boolean {
        val recentAirqualityForecast = airqualityDao.getAll()
        if(recentAirqualityForecast.isEmpty()){
            return false
        }
        return true
    }
}