package com.example.helse.ui.airquality

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.helse.R
import com.example.helse.data.api.AirqualityResponse
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.AirqualityRepository
import com.example.helse.viewmodels.AirqualityViewModel
import kotlinx.android.synthetic.main.fragment_airquality.*

class AirqualityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_airquality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        infoButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.airquality_to_information)
        }

        // defaultLocation == user location or defined location during setup
        val defaultLocation = requireActivity().intent.getParcelableExtra("LOCATION")
            ?: Location("Alnabru", "Oslo", 2.00, 2.12, "NO0057A")

        location.text =
            getString(R.string.location_text, defaultLocation.location, defaultLocation.superlocation)

        val airqualityViewModel = ViewModelProviders.of(this).get(AirqualityViewModel::class.java)
            .apply {
                airquality = AirqualityRepository(
                    AirqualityResponse(
                        defaultLocation,
                        this@AirqualityFragment
                    )
                )
            }

        var riskValue = ""

        airqualityViewModel.getAirqualityForecast().observe(this, Observer { forecast ->
            o3_concentration.text =
                getString(R.string.concentration, forecast.o3_concentration)
            no2_concentration.text =
                getString(R.string.concentration, forecast.no2_concentration)
            pm10_concentration.text =
                getString(R.string.concentration, forecast.pm10_concentration)
            pm25_concentration.text =
                getString(R.string.concentration, forecast.pm25_concentration)
            riskValue = forecast.riskValue
            val gaugeUri = "@drawable/gauge_${riskValue.toLowerCase()}"
            val res: Drawable = resources.getDrawable(resources.getIdentifier(gaugeUri, null, activity?.packageName))
            gauge.setImageDrawable(res)
        })
    }
}
