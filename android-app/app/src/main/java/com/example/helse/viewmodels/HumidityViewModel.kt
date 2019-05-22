package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.HumidityForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HumidityViewModel : ViewModel() {
    lateinit var humidityRepository: HumidityForecastRepository
    lateinit var mLocation: Location

    fun getHumidityForecast() = forecasts

    private val forecasts: MutableLiveData<List<HumidityForecast>> by lazy {
        MutableLiveData<List<HumidityForecast>>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { humidityRepository.getForecast(mLocation) }
            forecasts.value = deferred.await()
        }
    }
}