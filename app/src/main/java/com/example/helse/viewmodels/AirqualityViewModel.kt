package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.repositories.AirqualityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AirqualityViewModel : ViewModel() {
    lateinit var airqualityRepository: AirqualityRepository

    private val forecasts: MutableLiveData<MutableList<AirqualityForecast>> by lazy {
        MutableLiveData<MutableList<AirqualityForecast>>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { airqualityRepository.fetchAirquality() }
            forecasts.value = deferred.await()
        }
    }

    fun getAirqualityForecast() = forecasts
}
