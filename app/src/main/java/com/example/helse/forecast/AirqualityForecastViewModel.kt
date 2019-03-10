package com.example.helse.forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AirqualityForecastViewModel : ViewModel() {
    lateinit var airqualityForecastRepository: AirqualityForecastRepository

    private val forecast: MutableLiveData<List<AirqualityForecast>> by lazy {
        MutableLiveData<List<AirqualityForecast>>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { airqualityForecastRepository.fetchAirqualityForecast() }
            forecast.value = deferred.await()
        }
    }

    fun getAirqualityForecast() = forecast
}
