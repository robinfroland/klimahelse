package com.example.helse.ui.airquality

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.helse.R
import com.example.helse.adapters.AirqualityHorizontalAdapter
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.utilities.*
import com.example.helse.viewmodels.AirqualityViewModel
import kotlinx.android.synthetic.main.fragment_airquality.*
import java.util.*
import java.text.SimpleDateFormat


class AirqualityFragment : Fragment() {

    lateinit var preferences: AppPreferences
    private lateinit var viewAdapter: AirqualityHorizontalAdapter
    private lateinit var viewModel: AirqualityViewModel
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var navController: NavController
    private var timeList = mutableListOf<AirqualityForecast>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        preferences = Injector.getAppPreferences(requireContext())
        return inflater.inflate(R.layout.fragment_airquality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        toolbar_title.text = navController.currentDestination?.label

        initViewModel()
        observeDataStream()

        viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewAdapter = AirqualityHorizontalAdapter(timeList, this)
        LinearSnapHelper().attachToRecyclerView(risk_list)
        risk_list.apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }

        val selectedLocation = Injector.getLocation(requireContext())
        location.text = selectedLocation.location

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                navController.navigate(R.id.airquality_to_information)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(this).get(AirqualityViewModel::class.java)
            .apply {
                airqualityRepository =
                    Injector.getAirqualityForecastRepository(requireContext(), preferences.getLocation())
            }
    }

    private fun observeDataStream() {
        viewModel.getAirqualityForecast().observe(viewLifecycleOwner, Observer { forecasts ->
            for (i in 0 until forecasts.size) {
                timeList.add(forecasts[i])
            }
            viewAdapter.notifyDataSetChanged()
            val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            setScreenToChosenTime(timeList[hourOfDay + OFFSET_FOR_HORIZONTAL_SLIDER], hourOfDay)
        })
    }

    private fun initGauge(forecast: AirqualityForecast) {
        val color = when (forecast.riskValue) {
            LOW_VALUE -> resources.getColor(R.color.colorDangerLow, null)
            MEDIUM_VALUE -> resources.getColor(R.color.colorDangerMedium, null)
            HIGH_VALUE -> resources.getColor(R.color.colorDangerHigh, null)
            else -> resources.getColor(R.color.colorDangerVeryHigh, null)
        }

        gauge.value = convertRiskToInt(forecast.riskValue)
        gauge.pointStartColor = color
        gauge.pointEndColor = color
        risk_label.text = forecast.riskValue
        risk_label.setTextColor(color)
        gauge_img.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    fun setScreenToChosenTime(forecast: AirqualityForecast, index: Int) {
        viewManager.scrollToPositionWithOffset(index + OFFSET_FOR_HORIZONTAL_SLIDER_CENTER, 0)
        o3_concentration.text = getString(R.string.concentration, forecast.o3_concentration)
        no2_concentration.text = getString(R.string.concentration, forecast.no2_concentration)
        pm10_concentration.text = getString(R.string.concentration, forecast.pm10_concentration)
        pm25_concentration.text = getString(R.string.concentration, forecast.pm25_concentration)
        initGauge(forecast)
        val date: Date = SimpleDateFormat(ORIGINAL_DATE_PATTERN, Locale(("NO"))).parse(forecast.from)
        val formattedDate = SimpleDateFormat(DATE_PATTERN, Locale(("NO"))).format(date)
        time_and_date.text = getString(R.string.time_and_date, formattedDate)
        viewAdapter.notifyDataSetChanged()
    }
}
