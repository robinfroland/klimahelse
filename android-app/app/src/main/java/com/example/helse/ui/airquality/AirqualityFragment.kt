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
import com.example.helse.adapters.HorisontalAdapter
import com.example.helse.data.entities.RiskCircles
import com.example.helse.utilities.*
import com.example.helse.viewmodels.AirqualityViewModel
import kotlinx.android.synthetic.main.fragment_airquality.*
import java.util.*
import java.text.SimpleDateFormat


class AirqualityFragment : Fragment() {

    lateinit var preferences: AppPreferences
    lateinit var viewModel: AirqualityViewModel
    private lateinit var viewAdapter: HorisontalAdapter
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var navController: NavController
    private var timeList = ArrayList<RiskCircles>()
    private var hourOfDay = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_airquality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        toolbar_title.text = navController.currentDestination?.label

        initViewModel()
        observeDataStream()

        viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewAdapter = HorisontalAdapter(timeList, this)
        LinearSnapHelper().attachToRecyclerView(risk_list)
        risk_list.apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }

        preferences = Injector.getAppPreferences(requireContext())

        val savedLocation = preferences.getLocation()
        location.text = "%s, %s".format(savedLocation.location, savedLocation.superlocation)

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
                airqualityRepository = Injector.getAirqualityForecastRepository(requireContext())
            }
    }

    private fun observeDataStream() {
        viewModel.getAirqualityForecast().observe(this, Observer { forecasts ->
            timeList.clear()
            for (i in 0..23) {
                timeList.add(
                    RiskCircles(
                        (i + 1),
                        forecasts[i].from,
                        forecasts[i].riskValue,
                        getString(R.string.concentration, forecasts[i].o3_concentration),
                        getString(R.string.concentration, forecasts[i].no2_concentration),
                        getString(R.string.concentration, forecasts[i].pm10_concentration),
                        getString(R.string.concentration, forecasts[i].pm25_concentration)
                    )
                )
            }

            hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            viewAdapter.notifyDataSetChanged()
            setScreenToChosenTime(hourOfDay + OFFSET_FOR_HORIZONTAL_SLIDER)
        })
    }

    private fun initGauge(forecast: RiskCircles) {
        val riskValue = convertRiskToInt(forecast.overallRiskValue)
        val color: Int

        color = when {
            riskValue > 2 -> resources.getColor(R.color.colorDangerMedium, null)
            riskValue > 3 -> resources.getColor(R.color.colorDangerHigh, null)
            riskValue > 4 -> resources.getColor(R.color.colorDangerVeryHigh, null)
            else -> resources.getColor(R.color.colorDangerLow, null)
        }
        gauge.value = riskValue
        gauge.pointStartColor = color
        gauge.pointEndColor = color
        gauge_text.text = getString(R.string.gauge_risiko, forecast.overallRiskValue)
        gauge_text.setTextColor(color)
        gauge_img.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

    }

    fun setScreenToChosenTime(time: Int) {
        viewManager.scrollToPositionWithOffset(time + OFFSET_FOR_HORIZONTAL_SLIDER_CENTER, 0)
        o3_concentration.text = timeList[time].o3_concentration
        no2_concentration.text = timeList[time].no2_concentration
        pm10_concentration.text = timeList[time].pm10_concentration
        pm25_concentration.text = timeList[time].pm25_concentration
        initGauge(timeList[time])
        val date: Date = SimpleDateFormat(ORIGINAL_DATE_PATTERN, Locale(("NO"))).parse(timeList[time].dateAndDay)
        val formattedDate = SimpleDateFormat(DATE_PATTERN, Locale(("NO"))).format(date)
        time_and_date.text = getString(R.string.time_and_date, formattedDate)
        viewAdapter.notifyDataSetChanged()
    }
}
