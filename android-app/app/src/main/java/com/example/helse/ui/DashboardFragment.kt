package com.example.helse.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import com.example.helse.R
import com.example.helse.adapters.ModuleAdapter
import com.example.helse.data.api.AirqualityForecastApi
import com.example.helse.data.api.HumidityForecastApi
import com.example.helse.data.api.UvApi
import com.example.helse.data.api.UvForecastApi
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.Module
import com.example.helse.data.repositories.AirqualityForecastRepository
import com.example.helse.data.repositories.HumidityForecastRepository
import com.example.helse.data.repositories.UvForecastRepository
import com.example.helse.utilities.*
import com.example.helse.viewmodels.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var allModules: ArrayList<Module>
    private lateinit var enabledModules: ArrayList<Module>
    private lateinit var viewAdapter: ModuleAdapter
    private lateinit var airqualityModule: Module
    private lateinit var uvModule: Module
    private lateinit var humidityModule: Module
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("1")
        preferences = Injector.getAppPreferences(requireContext())
        println("2")
        enabledModules = arrayListOf()
        println("3")
        viewAdapter = ModuleAdapter(enabledModules)
        println("4")

        initModules()
        println("5")
        initViewModel()
        println("6")
        observeRiskLabels()
        println("7")

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("8")
        super.onViewCreated(view, savedInstanceState)
        println("9")
        initViewModel()
        println("10")
        observeRiskLabels()
        println("11")
        search_dashboard.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.dashboard_to_search)
        }
        println("12")

        module_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
        println("13")
    }

    override fun onStart() {
        println("14")
        super.onStart()
        println("15")
        initModules()
        println("16")
        initViewModel()
        println("17")
        observeRiskLabels()
        println("18")
    }

    override fun onResume() {
        println("19")
        super.onResume()
        println("20")
        updateUi()
        println("21")
    }

    private fun initViewModel() {
        println("Init view model")
        val location = preferences.getLocation()
        val database = LocalDatabase.getInstance(requireContext())
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
            .apply {
                airqualityForecastRepository =
                    AirqualityForecastRepository(database.airqualityDao(), AirqualityForecastApi(location))
                humidityForecastRepository =
                    HumidityForecastRepository(database.humidityDao(), HumidityForecastApi(location))
                uvForecastRepository = UvForecastRepository(database.uvDao(), UvForecastApi)
            }
        println("End init view model")
    }

    private fun updateUi() {
        println("updateUI")
        val selectedLocation = preferences.getLocation()
        search_dashboard.text = "%s, %s".format(selectedLocation.location, selectedLocation.superlocation)

        enabledModules.clear()
        allModules.forEach {
            if (preferences.isModuleEnabled(it)) {
                enabledModules.add(it)
            }
        }

        if (enabledModules.isEmpty()) {
            dashboard_top.visibility = View.INVISIBLE
            empty_dashboard_text.visibility = View.VISIBLE
        } else {
            dashboard_top.visibility = View.VISIBLE
            empty_dashboard_text.visibility = View.GONE
        }

        viewAdapter.notifyDataSetChanged()
        println("end updateUI")
    }

    private fun observeRiskLabels() {
        println("observeRiskLabels")
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        viewModel.getAirqualityForecast().observe(viewLifecycleOwner, Observer { forecast ->
            println("airForecast $forecast")
            airqualityModule.dangerIndicator =
                forecast[0].riskValue
            viewAdapter.notifyDataSetChanged()
        })

        viewModel.getHumidityForecast().observe(viewLifecycleOwner, Observer { forecast ->
            println("humiForecast $forecast")
            humidityModule.dangerIndicator =
                forecast[0].riskValue
            viewAdapter.notifyDataSetChanged()
        })

        viewModel.getUvForecast().observe(viewLifecycleOwner, Observer { forecast ->
            println("uvForecast $forecast")
            uvModule.dangerIndicator =
                forecast[0].riskValue
            viewAdapter.notifyDataSetChanged()
        })
        println("end observeRiskLabels")
    }

    private fun initModules() {
        println("initModules")
        airqualityModule = Module(
            AIRQUALITY_MODULE, R.drawable.ic_airquality_2x, "Luftkvalitet"
        )

        uvModule = Module(
            UV_MODULE, R.drawable.ic_uv_2x, "UV-stråling"
        )

        humidityModule = Module(
            HUMIDITY_MODULE, R.drawable.ic_humidity_2x, "Luftfuktighet"
        )

        allModules = arrayListOf(airqualityModule, uvModule, humidityModule)
        println("end initModules")
    }
}



