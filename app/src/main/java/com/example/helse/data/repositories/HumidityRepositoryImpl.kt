package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import com.example.helse.data.api.HumidityApi
import com.example.helse.data.entities.HumidityForecast

interface HumidityRepository {
    suspend fun fetchHumidity(): MutableList<HumidityForecast>
}

class HumidityRepositoryImpl(
    private val humidityApi : HumidityApi
) : HumidityRepository {

    @WorkerThread
    override suspend fun fetchHumidity(): MutableList<HumidityForecast> {
        return humidityApi.fetchHumidity()
    }
}