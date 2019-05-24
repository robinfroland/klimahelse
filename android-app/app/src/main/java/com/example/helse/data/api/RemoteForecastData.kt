package com.example.helse.data.api

import com.example.helse.data.entities.Location

interface RemoteForecastData<out T> {
    fun fetchForecast(location: Location, url: String = ""): List<T>
    fun buildCoordinateURI(latitude: Double, longitude: Double, url: String = ""): String
}