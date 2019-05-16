package com.example.helse.ui.onboarding

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.R

class LocationSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.location_settings, rootKey)
    }

    companion object {
        fun newInstance() = LocationSettingsFragment()
    }
}