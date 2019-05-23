package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.repositories.ForecastRepository
import com.example.helse.data.repositories.UvForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UvViewModel : ViewModel() {
    lateinit var uvRepository: ForecastRepository<UvForecast>
    lateinit var mLocation: Location

    fun getUvForecast() = forecasts

    private val forecasts: MutableLiveData<List<UvForecast>> by lazy {
        MutableLiveData<List<UvForecast>>().also {
            loadForecast()
        }
    }

    private fun loadForecast() {

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { uvRepository.getForecast(mLocation) }
            forecasts.value = deferred.await()
        }
    }
}
