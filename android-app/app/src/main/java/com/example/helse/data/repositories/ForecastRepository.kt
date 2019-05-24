package com.example.helse.data.repositories

import com.example.helse.data.entities.Location

interface ForecastRepository<out T> {
    fun getForecast(location: Location): List<T>
    fun fetchIsNeeded(location: Location): Boolean
}