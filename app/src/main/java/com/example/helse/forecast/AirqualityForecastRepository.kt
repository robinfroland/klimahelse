package com.example.helse.forecast

interface AirqualityForecastRepository {
    suspend fun fetchAirqualityForecast(): List<Any>
}

class AirqualityForecastRepositoryImpl(private val api: AirqualityForecastApi) : AirqualityForecastRepository {

    override suspend fun fetchAirqualityForecast(): List<Any> {
        return api.fetchAirquality()
    }
}
