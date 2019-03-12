package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.AirqualityForecast
import com.example.helse.data.AirqualityLocation
import com.example.helse.data.AirqualityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AirqualityViewModel : ViewModel() {
    lateinit var airqualityRepository: AirqualityRepository

    private val locations: MutableLiveData<List<AirqualityLocation>> by lazy {
        MutableLiveData<List<AirqualityLocation>>().also {
            loadLocations()
        }
    }

    private val forecast: MutableLiveData<AirqualityForecast> by lazy {
        MutableLiveData<AirqualityForecast>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { airqualityRepository.fetchAirquality() }
            forecast.value = deferred.await()
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { airqualityRepository.fetchLocations() }
            locations.value = deferred.await()
        }
    }

    fun getAirqualityForecast() = forecast
    fun getAirqualityLocations() = locations
}
