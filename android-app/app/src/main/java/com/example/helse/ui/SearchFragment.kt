package com.example.helse.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.R
import com.example.helse.adapters.LocationAdapter
import com.example.helse.data.api.LocationApi
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.alnabruLocation
import com.example.helse.data.repositories.LocationRepository
import com.example.helse.utilities.*
import com.example.helse.viewmodels.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var viewAdapter: LocationAdapter
    private lateinit var preferences: Preferences
    private lateinit var locationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = Injector.getAppPreferences(requireContext())
        initViewModel()
        submitData()
        setHasOptionsMenu(true)
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        preferences = Injector.getAppPreferences(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchIcon = menu.findItem(R.id.search_icon)
        val searchView = searchIcon.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(userInput: String): Boolean {
                if (userInput.isNotEmpty()) {
                    viewAdapter.filter.filter(userInput)
                }
                return false
            }
        })

        search_your_position.setOnClickListener {
            getDeviceLocation()
            val userposition = preferences.getLocation()
            locationClicked(userposition)
        }
    }

    private fun getDeviceLocation() {
        if (preferences.locationPermissionGranted()) {
            try {
                val location = locationClient.lastLocation
                location.addOnSuccessListener {
                    val currentLocation = location.result

                    // Look up elvis operator kotlin if you are confused what this does
                    val latitude = currentLocation?.latitude ?: alnabruLocation.latitude
                    val longtitude = currentLocation?.longitude ?: alnabruLocation.longitude

                    preferences.setLocation("Din posisjon", "", latitude, longtitude, "USERPOSITION")
                }
            } catch (e: SecurityException) {
                println(e)
            }
        } else {
            requestPermissions(
                arrayOf(LOCATION_PERMISSION),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    getDeviceLocation()
                } else {
                    "Velg område manuelt!".toast(context)
                }
            }
        }
    }

    private fun submitData() {
        viewModel.getLocations().observe(this, Observer { locations ->
            recyclerView_locations.layoutManager = LinearLayoutManager(context)
            viewAdapter = LocationAdapter { location: Location -> locationClicked(location) }
            recyclerView_locations.adapter = viewAdapter
            viewAdapter.submitList(locations)
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
            .apply {
                locationRepository = Injector.getLocationRepository(requireContext())
            }
    }

    private fun locationClicked(location: Location) {
        println("LOCATION CLICKED IS $location")
        preferences.setLocation(
            location.location,
            location.superlocation,
            location.latitude,
            location.longitude,
            location.stationID
        )
        val view = this.view ?: return
        Navigation.findNavController(view).navigate(R.id.search_to_dashboard)
    }
}
