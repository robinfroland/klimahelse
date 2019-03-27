package com.example.helse.data.repositories

import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.database.AirqualityDao
import com.example.helse.data.entities.AirqualityForecast

interface AirqualityRepository {
    suspend fun fetchAirquality(): AirqualityForecast
}

class AirqualityRepositoryImpl(
    private val airqualityDao: AirqualityDao,
    private val airqualityApi: AirqualityApi
) : AirqualityRepository {

    override suspend fun fetchAirquality(): AirqualityForecast {
        if(!haveRecentData()) {

            airqualityDao.insert(airqualityApi.fetchAirquality())
        }
        return airqualityDao.getRecent()
    }

    private fun haveRecentData() : Boolean {
        val recentAirqualityForecast = airqualityDao.getAll()
        if(recentAirqualityForecast.isEmpty()){
            return false
        }
        return true
    }

}