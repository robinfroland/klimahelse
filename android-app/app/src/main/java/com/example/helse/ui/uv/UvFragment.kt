package com.example.helse.ui.uv

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.helse.R
import com.example.helse.data.api.UvResponse
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.repositories.UvRepositoryImpl
import com.example.helse.utilities.Injector
import com.example.helse.utilities.convertRiskToInt
import com.example.helse.viewmodels.UvViewModel
import kotlinx.android.synthetic.main.fragment_uv.*

class UvFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_uv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        toolbar_title.text = navController.currentDestination?.label

        val preferences = Injector.getAppPreferences(requireContext())
        val selectedLocation = preferences.getLocation()
        location.text = "%s, %s".format(selectedLocation.location, selectedLocation.superlocation)

        val uvViewModel = ViewModelProviders.of(this).get(UvViewModel::class.java)
            .apply {
                uvRepository = UvRepositoryImpl(
                    LocalDatabase.getInstance(requireContext()).uvDao(),
                    UvResponse(
                        selectedLocation
                    ),
                    this@UvFragment,
                    selectedLocation
                )
            }

        uvViewModel.getUvForecast().observe(this, Observer { forecast ->
            val uvForecast = forecast[0]
            uviClear.text = uvForecast.uvClear.toString()
            uviPartlyCloudy.text = uvForecast.uvPartlyCloudy.toString()
            uviForecast.text = uvForecast.uvForecast.toString()
            uviCloudy.text = uvForecast.uvCloudy.toString()
            initGauge(uvForecast)
        })
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

    private fun initGauge(forecast: UvForecast) {
        val riskValue = convertRiskToInt(forecast.riskValue)
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
        gauge_text.text = getString(R.string.gauge_risiko, forecast.riskValue)
        gauge_text.setTextColor(color)
        gauge_img.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}
