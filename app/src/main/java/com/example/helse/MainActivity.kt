package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.api.forecast.AirqualityForecast
import com.example.helse.api.location.ApiLocation
import com.example.helse.api.location.LocationsApiImpl
import com.example.helse.api.location.LocationsRepositoryImpl
import com.example.helse.api.location.LocationsViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModels()
    }

    private fun setupViewModels() {
        val locationsViewModel = ViewModelProviders.of(this).get(LocationsViewModel::class.java).apply {
            locationsRepository =
                LocationsRepositoryImpl(LocationsApiImpl())
        }

        locationsViewModel.getAllLocations().observe(this, Observer { locations ->
            locationText.text = LocationStringBuilder.buildString(locations)
        })

    }
}

object ForecastStringBuilder {
    fun buildString(forecast: List<AirqualityForecast>): String {
        val forecastString = StringBuilder().append("Forecast:\n")
        forecast.forEach {
            forecastString.append(
                "\n${it.location.kommune}, ${it.location.name}\n" +
                        "${it.location.station}\n" +
                        "${it.Airquality.from}\n" +
                        "${it.Airquality.to}\n" +
                        "o3_concentration: ${it.Airquality.variables.o3_concentration}\n" +
                        "no2_concentration: ${it.Airquality.variables.no2_concentration}\n" +
                        "pm10_concentration: ${it.Airquality.variables.pm10_concentration}\n" +
                        "pm25_concentration: ${it.Airquality.variables.pm25_concentration}\n"
            )
        }
        return forecastString.toString()
    }
}

object LocationStringBuilder {
    fun buildString(locations: List<ApiLocation>): String {
        val locationString = StringBuilder().append("Locations:\n")
        locations.forEach { locationString.append(it.name + ", " + it.kommune + "\n" + ", " + it.station + "\n") }
        return locationString.toString()
    }
}
