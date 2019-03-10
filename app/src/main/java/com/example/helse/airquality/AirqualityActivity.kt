package com.example.helse.airquality

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.R
import kotlinx.android.synthetic.main.activity_airquality.*


class AirqualityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airquality)

        showLocations.setOnClickListener {
            startActivity(Intent(this, LocationListActivity::class.java))
        }


        // defaultLocation == user location or defined location during setup
        val defaultLocation = AirqualityLocation("Oslo", "Alnabru", "NO0057A")
        location.text = getString(R.string.location_text, defaultLocation.name, defaultLocation.kommune)



        val viewModel = ViewModelProviders.of(this).get(AirqualityViewModel::class.java).apply {
            airqualityRepository =
                AirqualityRepositoryImpl(AirqualityApiImpl(defaultLocation))
        }


        viewModel.getAirqualityForecast().observe(this, Observer { forecast ->
            o3.text = getString(R.string.o3, forecast.Airquality.variables.o3_concentration)
            no2.text = getString(R.string.no2, forecast.Airquality.variables.no2_concentration)
            pm10.text = getString(R.string.pm10, forecast.Airquality.variables.pm10_concentration)
            pm25.text = getString(R.string.pm25, forecast.Airquality.variables.pm25_concentration)

        })
    }

}
