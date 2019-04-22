package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.repositories.HumidityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HumidityViewModel : ViewModel() {

    lateinit var humidityRepository: HumidityRepository

    private val forecasts: MutableLiveData<MutableList<HumidityForecast>> by lazy {
        MutableLiveData<MutableList<HumidityForecast>>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { humidityRepository.fetchHumidity() }
            forecasts.value = deferred.await()
        }
    }

    fun getHumdityForecast() = forecasts
}