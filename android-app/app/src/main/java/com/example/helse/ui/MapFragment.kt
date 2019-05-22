package com.example.helse.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.helse.R
import com.example.helse.data.api.AirqualityForecastApi
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.AirqualityForecastRepository
import com.example.helse.utilities.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var preferences: AppPreferences
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = requireActivity().findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        preferences = Injector.getAppPreferences(requireContext())

        toolbar_title.text = findNavController().currentDestination?.label

        mapView.onResume()
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 == null) {
            return
        }
        map = p0

        GlobalScope.launch {
            runCatching {
                val locations = Injector.getLocationRepository(requireContext()).getAllLocations()

                addAirqualityToMap(p0, locations)
            }
        }
        val selectedLocation = preferences.getLocation()
        val zoomToCoordinate = LatLng(selectedLocation.latitude, selectedLocation.longitude)

        // Set bounds so user can not zoom outside Norway
        val southmostPointInNorway = LatLng(57.711257, 4.810990)
        val northmostPointNorway = LatLng(70.996689, 33.489198)
        map.setLatLngBoundsForCameraTarget(LatLngBounds(southmostPointInNorway, northmostPointNorway))

        //Zoom camera to users selected location
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(zoomToCoordinate, 13.toFloat()))
    }

    private suspend fun addAirqualityToMap(
        p0: GoogleMap,
        locations: MutableList<Location>
    ) {
        GlobalScope.launch {
            runCatching {
                for (i in 0 until locations.size) {
                    val location = locations[i]
                    val forecast = getForecastForLocationAsync(location).await()

                    var color = when (forecast.riskValue) {
                        LOW_VALUE -> R.color.colorDangerLowAlpha
                        MEDIUM_VALUE -> R.color.colorDangerMediumAlpha
                        HIGH_VALUE -> R.color.colorDangerHighAlpha
                        VERY_HIGH_VALUE -> R.color.colorDangerVeryHighAlpha
                        else -> R.color.colorGreyDark
                    }
                    color = ContextCompat.getColor(context!!, color)
                    requireActivity().runOnUiThread {
                        addCircleToMap(p0, location, color)
                    }
                    if (i == locations.size-1) progress_spinner.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun addCircleToMap(p0: GoogleMap, location: Location, riskColor: Int) {
        p0.addCircle(
            CircleOptions().center(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            ).fillColor(riskColor).strokeColor(Color.TRANSPARENT).radius(
                500.00
            ).visible(true)
        )
    }

    private fun getForecastForLocationAsync(location: Location): Deferred<AirqualityForecast> {
        return GlobalScope.async {
            val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

            val airqualityRepo = AirqualityForecastRepository(
                LocalDatabase.getInstance(requireContext()).airqualityDao(),
                AirqualityForecastApi()
            )

            val airquality = airqualityRepo.getForecast(location)
            airquality[hourOfDay]
        }
    }
}

