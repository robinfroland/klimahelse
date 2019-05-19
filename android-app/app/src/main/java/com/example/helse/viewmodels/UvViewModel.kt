package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.repositories.UvForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UvViewModel : ViewModel() {

    lateinit var uvRepository: UvForecastRepository

    private val forecasts: MutableLiveData<UvForecast> by lazy {
        MutableLiveData<UvForecast>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { uvRepository.fetchUv() }
            forecasts.value = deferred.await()
        }
    }

    fun getUvForecast() = forecasts
}
