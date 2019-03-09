package com.example.helse.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.ForecastStringBuilder
import com.example.helse.R
import com.example.helse.api.forecast.AirqualityForecastApiImpl
import com.example.helse.api.forecast.AirqualityForecastRepositoryImpl
import com.example.helse.api.forecast.AirqualityForecastViewModel
import com.example.helse.api.location.ApiLocation


class AirqualityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airquality)

        // Get data passed when activity was initiated
        val station = intent.getStringExtra("station")

        val viewModel = ViewModelProviders.of(this).get(AirqualityForecastViewModel::class.java).apply {
            airqualityForecastRepository =
                AirqualityForecastRepositoryImpl(AirqualityForecastApiImpl(station))
        }

        viewModel.getAirqualityForecast().observe(this, Observer { airqualityForecast ->
            findViewById<TextView>(R.id.airquality_data).text = ForecastStringBuilder.buildString(airqualityForecast)
        })
    }

}

fun startActivityAirquality(fromContext: Context, location: ApiLocation) {

    val intent = Intent(fromContext, AirqualityActivity::class.java)
    intent.putExtra("station", location.station)

    fromContext.startActivity(intent)
}
