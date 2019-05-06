package com.example.helse.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.R
import com.example.helse.data.api.AirqualityResponse
import com.example.helse.data.api.LocationResponse
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.AirqualityRepositoryImpl
import com.example.helse.data.repositories.LocationRepositoryImpl
import com.example.helse.utilities.HIGH_AQI_VALUE
import com.example.helse.utilities.LOW_AQI_VALUE
import com.example.helse.utilities.MEDIUM_AQI_VALUE
import com.example.helse.utilities.VERY_HIGH_AQI_VALUE
import com.example.helse.viewmodels.AirqualityViewModel
import com.example.helse.viewmodels.MapViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    private lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        mapView = requireActivity().findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        mapView.onResume()
        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapView.getMapAsync(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
            .apply {
                locationRepository = LocationRepositoryImpl(
                    LocalDatabase.getInstance(requireContext()).locationDao(),
                    LocationResponse(this@MapFragment.activity)
                )
            }
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 == null) {
            return
        }
        map = p0

        viewModel.getLocations().observe(this, Observer { locations ->
            addAirqualityToMap(map, locations)
        })

        val alnabru = LatLng(59.932141, 10.846132)

        // Set bounds so user can not zoom outside Norway
        val southmostPointInNorway = LatLng(57.711257, 4.810990)
        val northmostPointNorway = LatLng(70.996689, 33.489198)
        map.setLatLngBoundsForCameraTarget(LatLngBounds(southmostPointInNorway, northmostPointNorway))

        //Zoom camera to "alnabru", this should later be the users current position
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(alnabru, 12.toFloat()))
    }

    private fun addAirqualityToMap(p0: GoogleMap, locations: MutableList<Location>) {
        for (location in locations) {
            val airqualityViewModel = ViewModelProviders.of(this).get(AirqualityViewModel::class.java)
                .apply {
                    airqualityRepository = AirqualityRepositoryImpl(
                        LocalDatabase.getInstance(requireContext()).airqualityDao(),
                        AirqualityResponse(
                            location, this@MapFragment
                        ),
                        this@MapFragment
                    )
                }

            airqualityViewModel.getAirqualityForecast().observe(this, Observer { forecast ->
                val hourOfDay = Calendar.HOUR_OF_DAY
                var color = when (forecast[hourOfDay].riskValue) {
                    LOW_AQI_VALUE -> R.color.greenLowRisk
                    MEDIUM_AQI_VALUE -> R.color.yellowMediumRisk
                    HIGH_AQI_VALUE -> R.color.orangeHighRisk
                    VERY_HIGH_AQI_VALUE -> R.color.redVeryHighRisk
                    else -> R.color.colorGreyDark
                }
                color = ContextCompat.getColor(context!!, color)

                p0.addCircle(
                    CircleOptions().center(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    ).fillColor(color).strokeColor(Color.TRANSPARENT).radius(
                        250.00
                    ).visible(true)
                )
            })
        }
    }
}

