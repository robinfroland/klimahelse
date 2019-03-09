package com.example.helse.api.location

interface LocationsRepository {
    suspend fun fetchLocations(): List<Locations>
}

class LocationsRepositoryImpl(private val api: LocationsApi) :
    LocationsRepository {

    override suspend fun fetchLocations(): List<Locations> {
        // TODO: check if load from disk (implement a Room Database?) or fetch from server
        // if (dataIsStale) {
        //      fetch data from api, store to disk and return value
        // } else {
        //      load data from disk
        // }

        return api.fetchLocations()
    }
}