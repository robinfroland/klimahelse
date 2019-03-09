package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import com.example.helse.airquality.*
import com.example.helse.locations.*




class MainActivity : AppCompatActivity() {

    private val locations = ArrayList<Locations>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationText.text = LocationStringBuilder.buildString(locations)

        val locationsViewModel = ViewModelProviders.of(this).get(LocationsViewModel::class.java).apply {
            locationsRepository = LocationsRepositoryImpl(LocationsApiImpl())
        }

        locationsViewModel.getAllLocations().observe(this, Observer { locations ->
            locationText.text = LocationStringBuilder.buildString(locations)
        })
        println("locationsResponse: ${locationText.text}")


        val airqualityViewModel = ViewModelProviders.of(this).get(AirqualityForecastViewModel::class.java).apply {
            airqualityForecastRepository = AirqualityForecastRepositoryImpl(AirqualityForecastApiImpl())
        }

        airqualityViewModel.getAirqualityForecast().observe(this, Observer { airqualityForecast ->
            dataText.text = ForecastStringBuilder.buildString(airqualityForecast)
        })
        println("airqualityResponse: ${dataText.text}")

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
    fun buildString(locations: List<Locations>): String {
        val locationString = StringBuilder().append("Locations:\n")
        locations.forEach { locationString.append(it.name + ", " + it.kommune + "\n" + ", " + it.stasjon + "\n") }
        return locationString.toString()
    }
}
