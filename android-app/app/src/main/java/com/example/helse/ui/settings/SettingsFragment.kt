package com.example.helse.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.helse.R
import com.example.helse.utilities.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var preferences: AppPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_settings, rootKey)

        preferences = Injector.getAppPreferences(requireContext())
        val locationSwitch = findPreference<SwitchPreference>(USE_DEVICE_LOCATION)

        locationSwitch?.setOnPreferenceClickListener {
            if (preferences.useDeviceLocation()) {
                getDeviceLocation()
            }
            true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDivider(null)
    }

    private fun getDeviceLocation() {
        val deviceLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        if (preferences.locationPermissionGranted()) {
            try {
                deviceLocationClient.lastLocation.addOnSuccessListener {
                    if (it != null && it.latitude != 0.0 && it.longitude != 0.0) {
                        preferences.setDeviceLocation(it.latitude, it.longitude)
                    } else {
                        getString(R.string.failed_location_query).toast(requireContext())
                    }
                }
            } catch (e: SecurityException) {
            }
        } else {
            requestPermissions(
                arrayOf(LOCATION_PERMISSION),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    getDeviceLocation()
                } else {
                    getString(R.string.permission_denied_toast).toast(requireContext())
                }
            }
        }
    }
}
