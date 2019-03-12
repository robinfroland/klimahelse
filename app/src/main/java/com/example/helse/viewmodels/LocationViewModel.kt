package com.example.helse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.Locations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    lateinit var locations: Locations

    private val getLocations:  MutableLiveData<List<Location>> by lazy {
        MutableLiveData<List<Location>>().also {
            loadLocations()
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { locations.getAllLocations() }
            getLocations.value = deferred.await()
        }
    }

    fun getAllLocations() = getLocations
}
