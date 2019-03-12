package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.repositories.Airquality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AirqualityViewModel : ViewModel() {
    lateinit var airquality: Airquality

    private val forecast: MutableLiveData<AirqualityForecast> by lazy {
        MutableLiveData<AirqualityForecast>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { airquality.fetchAirquality() }
            forecast.value = deferred.await()
        }
    }

    fun getAirqualityForecast() = forecast
}
