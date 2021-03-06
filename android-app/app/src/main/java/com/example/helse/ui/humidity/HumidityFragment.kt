package com.example.helse.ui.humidity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.helse.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.helse.adapters.HumidityHorizontalAdapter
import com.example.helse.data.api.HumidityForecastApi
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.HumidityForecastRepository
import com.example.helse.utilities.*
import com.example.helse.utilities.Injector
import com.example.helse.viewmodels.HumidityViewModel
import kotlinx.android.synthetic.main.fragment_humidity.*
import java.text.SimpleDateFormat
import java.util.*

class HumidityFragment : Fragment() {

    private lateinit var viewAdapter: HumidityHorizontalAdapter
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var navController: NavController
    private lateinit var viewModel: HumidityViewModel
    private lateinit var location: Location
    private var timeList = mutableListOf<HumidityForecast>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_humidity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location = Injector.getLocation(requireContext())

        navController = Navigation.findNavController(view)
        toolbar_title.text = navController.currentDestination?.label

        initViewModel()
        observeDataStream()

        viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewAdapter = HumidityHorizontalAdapter(timeList, this)
        LinearSnapHelper().attachToRecyclerView(risk_list)
        risk_list.apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }

        location_text.text = location.location


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                navController.navigate(R.id.humidity_to_information)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViewModel() {
        val location = Injector.getLocation(requireContext())
        viewModel = ViewModelProviders.of(this).get(HumidityViewModel::class.java)
            .apply {
                humidityRepository = Injector.getHumidityForecastRepository(requireContext())
                mLocation = location
            }
    }

    private fun observeDataStream() {
        viewModel.getHumidityForecast().observe(this, Observer { forecasts ->
            timeList.clear()
            for (i in 0 until 24) {
                timeList.add(forecasts[i])
            }

            viewAdapter.notifyDataSetChanged()
            setSliderToChosenTime(forecasts[1 + OFFSET_FOR_HORIZONTAL_SLIDER], 1)
        })
    }

    private fun initGauge(forecast: HumidityForecast) {
        val color = when (forecast.riskValue) {
            LOW_HUMIDITY_VALUE -> resources.getColor(R.color.colorSaharaYellow, null)
            GOOD_HUMIDITY_VALUE -> resources.getColor(R.color.colorPrimary, null)
            else -> resources.getColor(R.color.colorPrimaryDark, null)
        }

        gauge.pointStartColor = color
        gauge.pointEndColor = color
        gauge.value = forecast.humidityValue.toInt()
        risk_label.setTextColor(color)
        risk_label.text = forecast.riskValue
        humidity_percentage.setTextColor(color)
        humidity_percentage.text = getString(R.string.precentage, forecast.humidityValue)
        gauge_img.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    fun setSliderToChosenTime(forecast: HumidityForecast, index: Int) {
        viewManager.scrollToPositionWithOffset(index + OFFSET_FOR_HORIZONTAL_SLIDER_CENTER, 0)
        initGauge(forecast)
        val date: Date = SimpleDateFormat(ORIGINAL_DATE_PATTERN, Locale(("NO"))).parse(forecast.from)
        val formattedDate = SimpleDateFormat(DATE_PATTERN, Locale(("NO"))).format(date)
        time_and_date.text = getString(R.string.time_and_date, formattedDate)
        viewAdapter.notifyDataSetChanged()
    }
}
