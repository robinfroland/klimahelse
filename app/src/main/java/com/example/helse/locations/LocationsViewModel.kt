package com.example.helse.locations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LocationsViewModel : ViewModel() {
    lateinit var locationsRepository: LocationsRepository

    private val locations: MutableLiveData<List<Locations>> by lazy {
        MutableLiveData<List<Locations>>().also {
            loadLocations()
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { locationsRepository.fetchLocations() }
            locations.value = deferred.await()
        }
    }

    fun getAllLocations() = locations
}