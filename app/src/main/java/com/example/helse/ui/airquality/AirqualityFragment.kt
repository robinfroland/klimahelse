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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.helse.R
import com.example.helse.adapters.HorisontalAdapter
import com.example.helse.data.api.AirqualityResponse
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.RiskCircles
import com.example.helse.data.repositories.AirqualityRepositoryImpl
import com.example.helse.utilities.OFFSET_FOR_HORIZONTAL_SLIDER
import com.example.helse.viewmodels.AirqualityViewModel
import kotlinx.android.synthetic.main.fragment_airquality.*
import java.util.*

class AirqualityFragment : Fragment() {

    private lateinit var viewAdapter: HorisontalAdapter
    private lateinit var viewManager: LinearLayoutManager
    private var timeList = ArrayList<RiskCircles>()
    private var hourOfDay = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_airquality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewAdapter = HorisontalAdapter(timeList, this)
        LinearSnapHelper().attachToRecyclerView(risk_list)
        risk_list.apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }

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
                airqualityRepository = AirqualityRepositoryImpl(
                    AirqualityResponse(
                        defaultLocation,
                        this@AirqualityFragment
                    )
                )
            }

        airqualityViewModel.getAirqualityForecast().observe(this, Observer { forecasts ->
            timeList.clear()
            for (i in 0..23) {
                val gaugeUri = "@drawable/gauge_${forecasts[i].riskValue.toLowerCase()}"
                println("RISKVALUE ${forecasts[i].riskValue.toLowerCase()}")
                val res: Drawable =
                    resources.getDrawable(resources.getIdentifier(gaugeUri, null, activity?.packageName), null)
                timeList.add(
                    RiskCircles(
                        (i + 1),
                        forecasts[i].riskValuee
                        getString(R.string.concentration, forecasts[i].o3_concentration),
                        getString(R.string.concentration, forecasts[i].no2_concentration),
                        getString(R.string.concentration, forecasts[i].pm10_concentration),
                        getString(R.string.concentration, forecasts[i].pm25_concentration),
                        res
                    )
                )
            }

            hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + OFFSET_FOR_HORIZONTAL_SLIDER
            viewAdapter.notifyDataSetChanged()
            setScreenToChosenTime(hourOfDay)
        })
    }

    fun setScreenToChosenTime(time: Int) {
        viewManager.scrollToPosition(time)
        o3_concentration.text = timeList[time].o3_concentration
        no2_concentration.text = timeList[time].no2_concentration
        pm10_concentration.text = timeList[time].pm10_concentration
        pm25_concentration.text = timeList[time].pm25_concentration
        gauge.setImageDrawable(timeList[time].gaugeImg)
        viewAdapter.notifyDataSetChanged()
    }
}
