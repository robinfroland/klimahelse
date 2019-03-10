package com.example.helse.airquality

interface AirqualityRepository {
    suspend fun fetchAirquality(): List<AirqualityForecast>
    suspend fun fetchLocations(): List<AirqualityLocation>
}

class AirqualityRepositoryImpl(private val api: AirqualityApi) :
    AirqualityRepository {

    override suspend fun fetchLocations(): List<AirqualityLocation> {
        // TODO: check if load from disk (implement a Room Database?) or fetch from server
        return api.fetchAirqualityLocations()
    }

    override suspend fun fetchAirquality(): List<AirqualityForecast> {
        return api.fetchAirquality()
    }
}