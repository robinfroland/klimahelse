package com.example.helse.forecast

interface AirqualityForecastRepository {
    suspend fun fetchAirqualityForecast(): List<AirqualityForecast>
}

class AirqualityForecastRepositoryImpl(private val api: AirqualityForecastApi) : AirqualityForecastRepository {

    override suspend fun fetchAirqualityForecast(): List<AirqualityForecast> {
        return api.fetchAirquality()
    }
}
