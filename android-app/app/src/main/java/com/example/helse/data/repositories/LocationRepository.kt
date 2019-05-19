package com.example.helse.data.repositories

import com.example.helse.data.api.LocationApi
import com.example.helse.data.database.LocationDao
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.emptyLocation

class LocationRepository(
    private val locationDao: LocationDao,
    private val locationApi: LocationApi
) {

    fun getAllLocations(): MutableList<Location> {
        if (!locationsExist()) {
            locationDao.insertAll(
                locationApi.fetchAllLocations()
            )
        }
        val locations = locationDao.getAllLocations()
        if (locations.size == 0) {
            return mutableListOf(emptyLocation)
        }
        return locations
    }

    private fun locationsExist(): Boolean {
        val locations = locationDao.getAllLocations()
        if (locations.isEmpty()) {
            return false
        }
        return true
    }


    companion object {

        private var INSTANCE: LocationRepository? = null

        // Singleton instantiation of repository

        fun getInstance(
            locationDao: LocationDao,
            locationApi: LocationApi
        ) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationRepository(locationDao, locationApi)
                    .also { INSTANCE = it }
            }
    }
}