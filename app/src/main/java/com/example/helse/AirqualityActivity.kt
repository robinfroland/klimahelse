package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.data.AirqualityApiImpl
import com.example.helse.data.entity.Location
import com.example.helse.data.AirqualityRepositoryImpl
import com.example.helse.viewmodels.AirqualityViewModel
import kotlinx.android.synthetic.main.activity_airquality.*


class AirqualityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airquality)

        showLocations.setOnClickListener {
            startActivity(Intent(this, LocationListActivity::class.java))
        }


        // defaultLocation == user location or defined location during setup
        val defaultLocation = Location("Alnabru", "Oslo", "NO0057A")
        location.text = getString(R.string.location_text, defaultLocation.location, defaultLocation.superlocation)



        val viewModel = ViewModelProviders.of(this).get(AirqualityViewModel::class.java).apply {
            airqualityRepository =
                AirqualityRepositoryImpl(
                    AirqualityApiImpl(
                        defaultLocation
                    )
                )
        }


        viewModel.getAirqualityForecast().observe(this, Observer { forecast ->
            o3_concentration.text = getString(R.string.o3_concentration, forecast.Airquality.variables.o3_concentration)
            no2_concentration.text = getString(R.string.no2_concentration, forecast.Airquality.variables.no2_concentration)
            pm10_concentration.text = getString(R.string.pm10_concentration, forecast.Airquality.variables.pm10_concentration)
            pm25_concentration.text = getString(R.string.pm25_concentration, forecast.Airquality.variables.pm25_concentration)

        })
    }

}
