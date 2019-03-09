package com.example.helse.airquality

interface AirqualityForecastRepository {
    suspend fun fetchAirqualityForecast(): List<AirqualityForecast>
}

class AirqualityForecastRepositoryImpl(private val api: AirqualityForecastApi) : AirqualityForecastRepository {

    override suspend fun fetchAirqualityForecast(): List<AirqualityForecast> {
        return api.fetchAirquality()
    }
}
