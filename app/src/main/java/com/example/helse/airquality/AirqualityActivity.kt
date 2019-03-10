package com.example.helse.airquality

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.ForecastStringBuilder
import com.example.helse.LocationStringBuilder
import com.example.helse.R


class AirqualityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airquality)

        // Get data passed when activity was initiated
        val station = intent.getStringExtra("station")

        val viewModel = ViewModelProviders.of(this).get(AirqualityViewModel::class.java).apply {
            airqualityRepository =
                AirqualityRepositoryImpl(AirqualityApiImpl(station))
        }


        viewModel.getAirqualityForecast().observe(this, Observer { airqualityForecast ->
            findViewById<TextView>(R.id.airquality_data).text = ForecastStringBuilder.buildString(airqualityForecast)
        })
    }

}

fun startActivityAirquality(fromContext: Context, airqualityLocation: AirqualityLocation) {

    val intent = Intent(fromContext, AirqualityActivity::class.java)
    intent.putExtra("station", airqualityLocation.station)

    fromContext.startActivity(intent)
}
