package com.example.helse.ui.uv

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.helse.R
import com.example.helse.data.api.UvForecastApi
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.repositories.UvForecastRepository
import com.example.helse.utilities.*
import com.example.helse.viewmodels.UvViewModel
import kotlinx.android.synthetic.main.fragment_uv.*
import kotlinx.android.synthetic.main.fragment_uv.gauge
import kotlinx.android.synthetic.main.fragment_uv.gauge_img
import kotlinx.android.synthetic.main.fragment_uv.risk_label
import kotlinx.android.synthetic.main.fragment_uv.toolbar_title

class UvFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: UvViewModel
    private lateinit var location: Location

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_uv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location = Injector.getLocation(requireContext())

        navController = Navigation.findNavController(view)
        toolbar_title.text = navController.currentDestination?.label

        initViewModel()
        observeDataStream()

        location_text.text = location.location
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                navController.navigate(R.id.uv_to_information)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(UvViewModel::class.java)
            .apply {
                uvRepository =
                    UvForecastRepository(
                        LocalDatabase.getInstance(requireContext()).uvDao(),
                        UvForecastApi()
                    )
                mLocation = location
            }

    }

    private fun observeDataStream() {
        viewModel.getUvForecast().observe(this, Observer { forecast ->
            val uvForecast = forecast[0]
            clear_uv.text = uvForecast.uvClear.toString()
            partly_cloudy_uv.text = uvForecast.uvPartlyCloudy.toString()
            forecast_uv.text = uvForecast.uvForecast.toString()
            cloudy_uv.text = uvForecast.uvCloudy.toString()
            initGauge(uvForecast)
        })
    }

    private fun initGauge(forecast: UvForecast) {
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
}
