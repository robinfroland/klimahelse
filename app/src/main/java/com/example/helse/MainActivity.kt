package com.example.helse

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.forecast.AirqualityForecast
import com.example.helse.forecast.AirqualityForecastApiImpl
import com.example.helse.forecast.AirqualityForecastRepositoryImpl
import com.example.helse.forecast.AirqualityForecastViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var text: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = "temp"

        val viewModel = ViewModelProviders.of(this).get(AirqualityForecastViewModel::class.java)
            .apply { airqualityForecastRepository = AirqualityForecastRepositoryImpl(AirqualityForecastApiImpl()) }

        viewModel.getAirqualityForecast().observe(
            this,
            Observer { airqualityForecast ->
                findViewById<TextView>(R.id.text).text = ForecastStringBuilder.buildString(airqualityForecast)
            })
        println("apiResponse: ${findViewById<TextView>(R.id.text).text}")
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
