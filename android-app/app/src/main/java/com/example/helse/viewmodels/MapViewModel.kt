package com.example.helse.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.LocationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    lateinit var locationRepository: LocationRepositoryImpl

    private val getLocations: MutableLiveData<MutableList<Location>> by lazy {
        MutableLiveData<MutableList<Location>>().also {
            loadLocations()
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { locationRepository.getAllLocations() }
            getLocations.value = deferred.await()
        }
    }

    fun getLocations() = getLocations
}