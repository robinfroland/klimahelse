package com.example.helse.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.R

class DashboardSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dashboard_settings, rootKey)
    }
}