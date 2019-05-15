package com.example.helse.data.repositories

import com.example.helse.data.api.LocationApi
import com.example.helse.data.database.LocationDao
import com.example.helse.data.entities.Location

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
        return locationDao.getAllLocations()
    }

    private fun locationsExist(): Boolean {
        val locations = locationDao.getAllLocations()
        if (locations.isEmpty()) {
            return false
        }
        return true
    }

    // Singleton instantiation
    companion object {
        @Volatile private var instance: LocationRepository? = null

        fun getInstance(locationDao: LocationDao, locationApi: LocationApi) =
            instance ?: synchronized(this) {
                instance ?: LocationRepository(locationDao, locationApi).also { instance = it }
            }
    }
}