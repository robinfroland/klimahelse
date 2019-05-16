package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.repositories.AirqualityForecastRepository
import com.example.helse.data.repositories.HumidityForecastRepository
import com.example.helse.data.repositories.UvForecastRepository
import kotlinx.coroutines.*

class DashboardViewModel : ViewModel() {
    lateinit var airqualityForecastRepository: AirqualityForecastRepository
    lateinit var humidityForecastRepository: HumidityForecastRepository
    lateinit var uvForecastRepository: UvForecastRepository



    private val airquality: MutableLiveData<MutableList<AirqualityForecast>> by lazy {
        MutableLiveData<MutableList<AirqualityForecast>>().also {
            loadAirquality()
        }
    }

    private fun loadAirquality() {

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { airqualityForecastRepository.fetchAirquality() }
            airquality.value = deferred.await()
        }
    }

    fun getAirqualityForecast() = airquality

    private val humidity: MutableLiveData<MutableList<HumidityForecast>> by lazy {
        MutableLiveData<MutableList<HumidityForecast>>().also {
            loadHumidity()
        }
    }

    private fun loadHumidity() {

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { humidityForecastRepository.fetchHumidity() }
            humidity.value = deferred.await()
        }
    }

    fun getHumidityForecast() = humidity
}