package com.example.helse.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.R
import com.example.helse.adapters.ModuleAdapter
import com.example.helse.data.entities.Module
import com.example.helse.utilities.*
import com.example.helse.viewmodels.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*

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

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = Injector.getAppPreferences(requireContext())

        initModules()
        initViewModel()

        search_dashboard.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.dashboard_to_search)
        }

        val viewManager = LinearLayoutManager(context)
        viewAdapter = ModuleAdapter(enabledModules)
        module_list.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        val selectedLocation = preferences.getLocation()
        search_dashboard.text = "%s, %s".format(selectedLocation.location, selectedLocation.superlocation)
        updateModules()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
            .apply {
                airqualityForecastRepository = Injector.getAirqualityForecastRepository(requireContext())
                humidityForecastRepository = Injector.getHumidityForecastRepository(requireContext())
                uvForecastRepository = Injector.getUvForecastRepository(requireContext())
            }
    }


    private fun updateModules() {
        // IF NONE ENABLED: SHOW TEXT
        enabledModules.clear()
        allModules.forEach {
            if (preferences.isModuleEnabled(it)) {
                enabledModules.add(it)
                it.pushEnabled = preferences.isNotificationEnabled(it)
            }
        }
        viewAdapter.notifyDataSetChanged()
    }

    private fun initModules() {
        airqualityModule = Module(
            AIRQUALITY_MODULE, R.drawable.ic_airquality_2x,
            "Luftkvalitet", "LOW", false
        )
        uvModule = Module(
            UV_MODULE, R.drawable.ic_uv_2x, "UV-stråling",
            "MEDIUM", false
        )
        humidityModule = Module(
            HUMIDITY_MODULE, R.drawable.ic_humidity_2x,
            "Luftfuktighet", "MEDIUM", false
        )

        allModules = arrayListOf(airqualityModule, uvModule, humidityModule)
        enabledModules = arrayListOf()
        updateModules()
    }
}



