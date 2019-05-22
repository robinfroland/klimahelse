package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.repositories.AirqualityForecastRepository
import com.example.helse.data.repositories.HumidityForecastRepository
import com.example.helse.data.repositories.UvForecastRepository
import com.example.helse.utilities.AIRQUALITY_MODULE
import com.example.helse.utilities.HUMIDITY_MODULE
import com.example.helse.utilities.UV_MODULE
import kotlinx.coroutines.*

class DashboardViewModel : ViewModel() {
    lateinit var airqualityForecastRepository: AirqualityForecastRepository
    lateinit var humidityForecastRepository: HumidityForecastRepository
    lateinit var uvForecastRepository: UvForecastRepository
    lateinit var mLocation: Location

    fun retryDatafetch(moduleKey: String) {
        when(moduleKey) {
            AIRQUALITY_MODULE -> loadAirqualityForecast()
            HUMIDITY_MODULE -> loadHumidityForecast()
            UV_MODULE -> loadUvData()
        }
    }

    fun getAirqualityForecast() = airqualityData

    fun getHumidityForecast() = humidityData

    fun getUvForecast() = uvData

    private val airqualityData: MutableLiveData<List<AirqualityForecast>> by lazy {
        MutableLiveData<List<AirqualityForecast>>().also {
            loadAirqualityForecast()
        }
    }

    private val humidityData: MutableLiveData<List<HumidityForecast>> by lazy {
        MutableLiveData<List<HumidityForecast>>().also {
            loadHumidityForecast()
        }
    }

    private val uvData: MutableLiveData<List<UvForecast>> by lazy {
        MutableLiveData<List<UvForecast>>().also {
            loadUvData()
        }
    }

    private fun loadAirqualityForecast() {
        viewModelScope.launch {
            airqualityData.value = withContext(Dispatchers.IO) {
                airqualityForecastRepository.getForecast(mLocation)
            }
        }
    }

    private fun loadHumidityForecast() {
        viewModelScope.launch {
            humidityData.value = withContext(Dispatchers.IO) {
                humidityForecastRepository.getForecast(mLocation)
            }
        }
    }

    private fun loadUvData() {
        viewModelScope.launch {
            uvData.value = withContext(Dispatchers.IO) {
                uvForecastRepository.getForecast(mLocation)
            }
        }
    }

}