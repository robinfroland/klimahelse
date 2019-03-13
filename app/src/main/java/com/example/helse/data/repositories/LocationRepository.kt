package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import com.example.helse.data.api.LocationApi
import com.example.helse.data.database.LocationDao
import com.example.helse.data.entities.Location

interface Locations {
    suspend fun getAllLocations(): MutableList<Location>
}

class LocationRepository(
    private val locationDao: LocationDao,
    private val locationApi: LocationApi
) : Locations {

    @WorkerThread
    override suspend fun getAllLocations(): MutableList<Location> {
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
}