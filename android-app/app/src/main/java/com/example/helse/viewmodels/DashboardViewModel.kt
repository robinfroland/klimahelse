package com.example.helse.viewmodels

import androidx.lifecycle.ViewModel
import com.example.helse.data.repositories.AirqualityForecastRepository
import com.example.helse.data.repositories.HumidityForecastRepository
import com.example.helse.data.repositories.UvForecastRepository

class DashboardViewModel : ViewModel() {
    lateinit var airqualityForecastRepository: AirqualityForecastRepository
    lateinit var humidityForecastRepository: HumidityForecastRepository
    lateinit var uvForecastRepository: UvForecastRepository

}