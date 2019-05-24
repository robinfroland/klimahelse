package com.example.helse.data.api

import com.example.helse.data.entities.Location

interface RemoteForecastData<out T> {
    fun fetchForecast(location: Location): List<T>
    fun buildCoordinateURI(latitude: Double, longitude: Double): String
}