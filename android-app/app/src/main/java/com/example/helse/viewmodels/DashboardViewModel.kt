package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.repositories.AirqualityForecastRepository
import com.example.helse.data.repositories.HumidityForecastRepository
import com.example.helse.data.repositories.UvForecastRepository
import kotlinx.coroutines.*

class DashboardViewModel : ViewModel() {
    lateinit var airqualityForecastRepository: AirqualityForecastRepository
    lateinit var humidityForecastRepository: HumidityForecastRepository
    lateinit var uvForecastRepository: UvForecastRepository

    fun getAirqualityForecast() = airqualityData

    fun getHumidityForecast() = humidityData

    fun getUvForecast() = uvData

    private val airqualityData: MutableLiveData<MutableList<AirqualityForecast>> by lazy {
        MutableLiveData<MutableList<AirqualityForecast>>().also {
            loadAirqualityForecast()
        }
    }

    private val humidityData: MutableLiveData<MutableList<HumidityForecast>> by lazy {
        MutableLiveData<MutableList<HumidityForecast>>().also {
            loadHumidityForecast()
        }
    }

    private val uvData: MutableLiveData<MutableList<UvForecast>> by lazy {
        MutableLiveData<MutableList<UvForecast>>().also {
            loadUvData()
        }
    }

    private fun loadAirqualityForecast() {
        viewModelScope.launch {
            airqualityData.value = withContext(Dispatchers.IO) {
                airqualityForecastRepository.fetchAirquality()
            }
        }
    }

    private fun loadHumidityForecast() {
        viewModelScope.launch {
            humidityData.value = withContext(Dispatchers.IO) {
                humidityForecastRepository.fetchHumidity()
            }
        }
    }

    private fun loadUvData() {
        viewModelScope.launch {
            uvData.value = withContext(Dispatchers.IO) {
                uvForecastRepository.fetchUv()
            }
        }
    }

}