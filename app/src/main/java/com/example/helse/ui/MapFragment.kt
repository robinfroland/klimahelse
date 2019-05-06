package com.example.helse.ui

import android.graphics.Color
import android.media.session.MediaSession
import android.os.Bundle
import android.os.Handler
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
import com.example.helse.data.entities.AirqualityForecast
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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

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

        GlobalScope.launch {
            val locations = LocationRepositoryImpl(
                LocalDatabase.getInstance(requireContext()).locationDao(),
                LocationResponse(this@MapFragment.requireActivity())
            ).getAllLocations()
            val riskScores = findRiskScoresForAllLocations(locations).await()

            requireActivity().runOnUiThread {
                addAirqualityToMap(map, riskScores)
            }
        }
        println("Oh shit, it shouldn`t be here")


        val alnabru = LatLng(59.932141, 10.846132)

        // Set bounds so user can not zoom outside Norway
        val southmostPointInNorway = LatLng(57.711257, 4.810990)
        val northmostPointNorway = LatLng(70.996689, 33.489198)
        map.setLatLngBoundsForCameraTarget(LatLngBounds(southmostPointInNorway, northmostPointNorway))

        //Zoom camera to "alnabru", this should later be the users current position
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(alnabru, 12.toFloat()))
    }

    private suspend fun findRiskScoresForAllLocations(
        locations: MutableList<Location>
    ): Deferred<HashMap<Location, Int>> {
        return GlobalScope.async {
            var riskscoreMap = HashMap<Location, Int>()
            for (i in 0 until locations.size) {
                println("location pre: ${locations[i]}")
                val location = locations[i]
                val forecast = getForecastForLocationAsync(location, this@MapFragment).await()

                println("location: $location")
                println("forecast: $forecast")
                var color = when (forecast.riskValue) {
                    LOW_AQI_VALUE -> R.color.greenLowRisk
                    MEDIUM_AQI_VALUE -> R.color.yellowMediumRisk
                    HIGH_AQI_VALUE -> R.color.orangeHighRisk
                    VERY_HIGH_AQI_VALUE -> R.color.redVeryHighRisk
                    else -> R.color.colorGreyDark
                }
                color = ContextCompat.getColor(context!!, color)
                riskscoreMap[location] = color
            }
            riskscoreMap
        }
    }

    private fun addAirqualityToMap(p0: GoogleMap, locations: HashMap<Location, Int>) {
        locations.forEach { (location, riskColor) ->
            p0.addCircle(
                CircleOptions().center(
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                ).fillColor(riskColor).strokeColor(Color.TRANSPARENT).radius(
                    250.00
                ).visible(true)
            )
        }
    }
}

fun getForecastForLocationAsync(location: Location, mapFragment: Fragment): Deferred<AirqualityForecast> {
    return GlobalScope.async {
        val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        println("hour of day: $hourOfDay")

        val airRepo = AirqualityResponse(location, mapFragment)
        val airquality = airRepo.fetchAirquality()
        println("airquality: $airquality")
        airquality[hourOfDay]
    }
}
