package com.example.helse.ui.humidity

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.helse.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.helse.data.api.HumidityForecastApi
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.repositories.HumidityForecastRepository
import com.example.helse.utilities.Injector
import com.example.helse.viewmodels.HumidityViewModel
import kotlinx.android.synthetic.main.fragment_humidity.*

class HumidityFragment : Fragment() {

    private lateinit var navController: NavController

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

        val preferences = Injector.getAppPreferences(requireContext())
        val selectedLocation = preferences.getLocation()
        location.text = "%s, %s".format(selectedLocation.location, selectedLocation.superlocation)

        val humidityViewModel = ViewModelProviders.of(this).get(HumidityViewModel::class.java)
            .apply {
                humidityRepository = Injector.getHumidityForecastRepository(requireContext())
            }

        humidityViewModel.getHumdityForecast().observe(this, Observer { forecast ->
            val humidityForecast = forecast[0]
            gauge.value = humidityForecast.humidityValue.toInt()
            risk_value.text = humidityForecast.riskValue
            gauge_text.text = getString(R.string.precentage, humidityForecast.humidityValue)
        })
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

}
