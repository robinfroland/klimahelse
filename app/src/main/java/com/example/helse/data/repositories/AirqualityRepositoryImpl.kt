package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.entities.AirqualityForecast

interface AirqualityRepository {
    suspend fun fetchAirquality(): MutableList<AirqualityForecast>
}

class AirqualityRepositoryImpl(
    private val airqualityApi: AirqualityApi
) : AirqualityRepository {

    @WorkerThread
    override suspend fun fetchAirquality(): MutableList<AirqualityForecast> {
        return airqualityApi.fetchAirquality()
    }
}