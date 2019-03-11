package com.example.helse.data

import com.example.helse.data.entity.AirqualityForecast
import com.example.helse.data.entity.Location

interface AirqualityRepository {
    suspend fun fetchLocations(): List<Location>
    suspend fun fetchAirquality(): AirqualityForecast
}

class AirqualityRepositoryImpl(private val api: AirqualityApi) :
    AirqualityRepository {

    override suspend fun fetchLocations(): List<Location> {
        // TODO: check if load from disk (implement a Room Database?) or fetch from server
        return api.fetchAirqualityLocations()
    }

    override suspend fun fetchAirquality(): AirqualityForecast {
        return api.fetchAirquality()
    }

}