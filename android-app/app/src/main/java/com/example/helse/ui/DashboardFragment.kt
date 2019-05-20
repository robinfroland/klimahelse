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
import com.example.helse.R
import com.example.helse.adapters.ModuleAdapter
import com.example.helse.data.api.AirqualityForecastApi
import com.example.helse.data.api.HumidityForecastApi
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
        preferences = Injector.getAppPreferences(requireContext())
        enabledModules = arrayListOf()
        viewAdapter = ModuleAdapter(enabledModules)

        initModules()
        initViewModel()
        observeRiskLabels()

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observeRiskLabels()
        search_dashboard.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.dashboard_to_search)
        }

        module_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    private fun initViewModel() {
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
    }

    private fun updateUi() {
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
    }

    private fun observeRiskLabels() {
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        viewModel.getAirqualityForecast().observe(viewLifecycleOwner, Observer { forecast ->
            if (forecast.size < 24) {
                airqualityModule.dangerIndicator = RISK_NOT_AVAILABLE
            } else {
                airqualityModule.dangerIndicator =
                    forecast[currentTime].riskValue
            }
            viewAdapter.notifyDataSetChanged()
        })

        viewModel.getHumidityForecast().observe(viewLifecycleOwner, Observer { forecast ->
            if (forecast.size < 1) {
                humidityModule.dangerIndicator = RISK_NOT_AVAILABLE
            } else {
                humidityModule.dangerIndicator =
                    forecast[0].riskValue
            }
            viewAdapter.notifyDataSetChanged()
        })

        viewModel.getUvForecast().observe(viewLifecycleOwner, Observer { forecast ->
            if (forecast.size < 1) {
                uvModule.dangerIndicator = RISK_NOT_AVAILABLE
            } else {
                uvModule.dangerIndicator =
                    forecast[0].riskValue
            }
            viewAdapter.notifyDataSetChanged()
        })
    }

    private fun initModules() {
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
    }
}



