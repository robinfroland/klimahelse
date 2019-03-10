package com.example.helse

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.helse.airquality.AirqualityForecast
import com.example.helse.airquality.AirqualityLocation
import com.example.helse.airquality.startActivityAirquality
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    fun showStationData(v: View) {
        startActivityAirquality(this, AirqualityLocation("Skip", "Ostfold", "NO0057A"))
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
    fun buildString(airqualityLocations: List<AirqualityLocation>): String {
        val locationString = StringBuilder().append("Locations:\n")
        airqualityLocations.forEach { locationString.append(it.name + ", " + it.kommune + "\n" + ", " + it.station + "\n") }
        return locationString.toString()
    }
}
