package com.example.helse.ui.settings

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.R
import com.example.helse.utilities.Injector

class LocationSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.location_settings, rootKey)
        val preferences = Injector.getAppPreferences(requireContext())
        if (!preferences.isFirstLaunch()) {
            preferenceScreen.removePreference(findPreference("PREF_DESCRIPTION"))
        }
    }

    companion object {
        fun newInstance() = LocationSettingsFragment()
    }
}