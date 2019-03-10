package com.example.helse.airquality

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


        // TODO: Get data passed when activity was initiated
        // default == "min posisjon" eller valgt sted i setup
        val defaultLocation = AirqualityLocation("Skip", "Ostfold", "NO0057A")



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
