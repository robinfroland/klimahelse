package com.example.helse.ui.humidity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.helse.R
import com.example.helse.data.entities.Location
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.helse.data.api.HumidityResponse
import com.example.helse.data.repositories.HumidityRepositoryImpl
import com.example.helse.viewmodels.HumidityViewModel
import kotlinx.android.synthetic.main.fragment_humidity.*

class HumidityFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_humidity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        infoButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.humidity_to_information)
        }

        // defaultLocation == user location or defined location during setup
        val defaultLocation = requireActivity().intent.getParcelableExtra("LOCATION")
            ?: Location("Alnabru", "Oslo", 10.84655, 59.92767, "NO0057A")

        location.text =
            getString(R.string.location_text, defaultLocation.location, defaultLocation.superlocation)

        val humidityViewModel = ViewModelProviders.of(this).get(HumidityViewModel::class.java)
            .apply {
                humidityRepository = HumidityRepositoryImpl(
                    HumidityResponse(
                        defaultLocation,
                        this@HumidityFragment
                    )
                )
            }

        humidityViewModel.getHumdityForecast().observe(this, Observer { forecast ->
            val humidityForecast = forecast[0]
            gauge.value = humidityForecast.humidityValue.toInt()
            humidity_textView.text = getString(R.string.precentage, humidityForecast.humidityValue)
        })
    }

}
