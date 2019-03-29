package com.example.helse.ui.airquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.R
import com.example.helse.adapters.HorisontalAdapter
import com.example.helse.data.api.AirqualityResponse
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.AirqualityRepository
import com.example.helse.viewmodels.AirqualityViewModel
import kotlinx.android.synthetic.main.fragment_airquality.*

class AirqualityFragment : Fragment() {

    private lateinit var viewAdapter: HorisontalAdapter
    private var timeList = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_airquality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateHorizontalSlider()
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewAdapter = HorisontalAdapter(timeList)

        risk_list.apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }

        informationBtn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.airquality_to_information)
        }

        // defaultLocation == user location or defined location during setup
        val defaultLocation = requireActivity().intent.getParcelableExtra("LOCATION")
            ?: Location("Alnabru", "Oslo", 2.00, 2.12, "NO0057A")

        location.text = getString(R.string.location_text, defaultLocation.location, defaultLocation.superlocation)

        val airqualityViewModel = ViewModelProviders.of(this).get(AirqualityViewModel::class.java)
            .apply {
                airquality = AirqualityRepository(
                    AirqualityResponse(
                        defaultLocation,
                        this@AirqualityFragment
                    )
                )
            }

        airqualityViewModel.getAirqualityForecast().observe(this, Observer { forecast ->
            o3_concentration.text = getString(R.string.o3_concentration, forecast.Airquality.variables.o3_concentration)
            no2_concentration.text =
                getString(R.string.no2_concentration, forecast.Airquality.variables.no2_concentration)
            pm10_concentration.text =
                getString(R.string.pm10_concentration, forecast.Airquality.variables.pm10_concentration)
            pm25_concentration.text =
                getString(R.string.pm25_concentration, forecast.Airquality.variables.pm25_concentration)

        })
    }

    fun updateHorizontalSlider() {
        var i = 1
        while ( i <= 24) {
            timeList.add(i)
            i++
        }
    }
}
