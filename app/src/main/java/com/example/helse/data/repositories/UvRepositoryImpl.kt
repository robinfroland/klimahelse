package com.example.helse.data.repositories

import androidx.annotation.WorkerThread
import com.example.helse.data.api.UvApi
import com.example.helse.data.entities.UvForecast

interface UvRepository {
    suspend fun fetchUv(): MutableList<UvForecast>
}

class UvRepositoryImpl(private val uvApi: UvApi) : UvRepository {

    @WorkerThread
    override suspend fun fetchUv(): MutableList<UvForecast> {
        return uvApi.fetchUv()
    }
}