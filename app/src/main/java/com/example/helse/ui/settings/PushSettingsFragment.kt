package com.example.helse.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.R

class PushSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.push_settings, rootKey)
    }
}