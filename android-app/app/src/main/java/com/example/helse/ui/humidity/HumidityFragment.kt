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

import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.HumidityForecast

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

        val selectedLocation = Injector.getLocation(requireContext())
        location.text = "%s, %s".format(selectedLocation.location, selectedLocation.superlocation)


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
        viewModel = ViewModelProviders.of(this).get(HumidityViewModel::class.java)
            .apply {
                humidityRepository = Injector.getHumidityForecastRepository(requireContext())
            }
    }

    private fun observeDataStream() {
        viewModel.getHumdityForecast().observe(this, Observer { forecasts ->
            timeList.clear()
            for (i in 0 until 24){
                timeList.add(forecasts[i])
            }

            val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            viewAdapter.notifyDataSetChanged()
            setScreenToChosenTime(forecasts[hourOfDay + OFFSET_FOR_HORIZONTAL_SLIDER], hourOfDay)
        })
    }

    private fun initGauge(forecast: HumidityForecast) {

        val color = when(forecast.riskValue) {
            "LAV"  ->  resources.getColor(R.color.colorSaharaYellow, null)
            "PASSE"-> resources.getColor(R.color.colorPrimary, null)
            else   -> resources.getColor(R.color.colorPrimaryDark, null)
        }

        gauge.pointStartColor = color
        gauge.pointEndColor = color
        gauge_text.setTextColor(color)
        risk_value.setTextColor(color)
        gauge.value = forecast.humidityValue.toInt()
        risk_value.text = getString(R.string.gauge_humidity, forecast.riskValue)
        gauge_text.text = getString(R.string.precentage, forecast.humidityValue)
        gauge_img.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    fun setScreenToChosenTime(forecast: HumidityForecast, index : Int) {
        viewManager.scrollToPositionWithOffset(index + OFFSET_FOR_HORIZONTAL_SLIDER_CENTER, 0)
        initGauge(forecast)
        val date: Date = SimpleDateFormat(ORIGINAL_DATE_PATTERN, Locale(("NO"))).parse(forecast.from)
        val formattedDate = SimpleDateFormat(DATE_PATTERN, Locale(("NO"))).format(date)
        time_and_date.text = getString(R.string.time_and_date, formattedDate)
        viewAdapter.notifyDataSetChanged()
    }
}
