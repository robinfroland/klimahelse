package com.example.helse.ui.humidity

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.helse.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.helse.utilities.Injector
import com.example.helse.viewmodels.HumidityViewModel
import kotlinx.android.synthetic.main.fragment_humidity.*
import kotlinx.android.synthetic.main.fragment_humidity.gauge
import kotlinx.android.synthetic.main.fragment_humidity.gauge_text
import kotlinx.android.synthetic.main.fragment_humidity.location
import kotlinx.android.synthetic.main.fragment_humidity.toolbar_title

class HumidityFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: HumidityViewModel

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
        viewModel.getHumdityForecast().observe(this, Observer { forecast ->
            val humidityForecast = forecast[0]
            gauge.value = humidityForecast.humidityValue.toInt()
            value_label.text = humidityForecast.riskValue
            gauge_text.text = getString(R.string.precentage, humidityForecast.humidityValue)
        })
    }

}
