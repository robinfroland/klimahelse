package com.example.helse.ui.settings

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.R
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.Injector

class DashboardSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dashboard_settings, rootKey)
        val preferences = Injector.getAppPreferences(requireContext())
        if (!preferences.isFirstLaunch()) {
            preferenceScreen.removePreference(findPreference("PREF_DESCRIPTION"))
        }

    }

    companion object {
        fun newInstance() = DashboardSettingsFragment()
    }
}