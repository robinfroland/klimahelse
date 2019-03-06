package com.example.helse

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
        println("apiResponse: ${findViewById<TextView>(R.id.text).text.toString()}")
    }

}

object ForecastStringBuilder {
    fun buildString(forecast: List<Any>): String {
        val forecastString = StringBuilder().append("Forecast:\n")
        println("forecast: ${forecast}")
        forecast.forEach { forecastString.append(/*Things of the api..*/"hi") }
        return forecastString.toString()
    }
}
