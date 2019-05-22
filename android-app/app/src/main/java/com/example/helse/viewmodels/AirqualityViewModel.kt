package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.AirqualityForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AirqualityViewModel : ViewModel() {
    lateinit var airqualityRepository: AirqualityForecastRepository
    lateinit var mLocation: Location

    fun getAirqualityForecast() = forecasts

    private val forecasts: MutableLiveData<List<AirqualityForecast>> by lazy {
        MutableLiveData<List<AirqualityForecast>>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { airqualityRepository.getForecast(mLocation) }
            forecasts.value = deferred.await()
        }
    }
}
