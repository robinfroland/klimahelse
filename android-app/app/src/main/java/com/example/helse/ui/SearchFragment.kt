package com.example.helse.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.R
import com.example.helse.adapters.LocationAdapter
import com.example.helse.data.entities.Location
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
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = "Søk..."
        searchView.isIconified = false

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
            closeKeyboard()
            findNavController().navigate(R.id.search_to_dashboard)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Hide keyboard on navigate up
        closeKeyboard()
        return super.onOptionsItemSelected(item)
    }

    private fun getDeviceLocation() {
        val deviceLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        if (preferences.locationPermissionGranted()) {
            try {
                deviceLocationClient.lastLocation.addOnSuccessListener {
                    if (it != null && it.latitude != 0.0 && it.longitude != 0.0) {
                        preferences.setDeviceLocation(it.latitude, it.longitude)
                    } else {
                            getString(R.string.failed_location_query).toast(requireContext())
                    }
                }
            } catch (e: SecurityException) {
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
                        getString(R.string.permission_denied_toast).toast(requireContext())
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
        closeKeyboard()
        preferences.setLocation(
            location.location,
            location.superlocation,
            location.latitude,
            location.longitude,
            location.stationID
        )
        findNavController().navigate(R.id.search_to_dashboard)
    }

    private fun closeKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
