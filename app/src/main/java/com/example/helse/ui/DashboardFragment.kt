package com.example.helse.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.R
import com.example.helse.adapters.ModuleAdapter
import com.example.helse.data.entities.Module
import com.example.helse.utilities.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var allModules: ArrayList<Module>
    private lateinit var enabledModules: ArrayList<Module>
    private lateinit var viewAdapter: ModuleAdapter
    private lateinit var airqualityModule: Module
    private lateinit var uvModule: Module
    private lateinit var allergyModule: Module
    private lateinit var humidityModule: Module
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferences = Injector.getAppPreferences(requireContext())
        initModules()
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        submitModules()

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        super.onViewCreated(view, savedInstanceState)
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

    private fun submitModules() {
        enabledModules = allModules
    }

    private fun initModules() {
        airqualityModule = Module(
            AIRQUALITY_MODULE, R.drawable.ic_launcher_foreground,
            "Luftkvalitet", "HIGH", false
        )
        uvModule = Module(
            UV_MODULE, R.drawable.ic_launcher_foreground, "UV-stråling",
            "LOW", false
        )
        allergyModule = Module(
            ALLERGY_MODULE, R.drawable.ic_launcher_foreground,
            "Pollenspredning", "MEDIUM", false
        )
        humidityModule = Module(
            HUMIDITY_MODULE, R.drawable.ic_launcher_foreground,
            "Luftfuktighet", "MEDIUM", false
        )

        allModules = arrayListOf(airqualityModule, uvModule, humidityModule, allergyModule)
    }
}



