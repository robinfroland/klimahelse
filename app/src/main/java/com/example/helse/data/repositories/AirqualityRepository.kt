package com.example.helse.data.repositories

import com.example.helse.data.api.AirqualityApi
import com.example.helse.data.entities.AirqualityForecast

interface Airquality {
    suspend fun fetchAirquality(): AirqualityForecast
}

class AirqualityRepository(
    private val airqualityApi: AirqualityApi
) : Airquality {

    override suspend fun fetchAirquality(): AirqualityForecast {
        return airqualityApi.fetchAirquality()
    }

}