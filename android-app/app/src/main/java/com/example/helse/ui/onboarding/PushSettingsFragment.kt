package com.example.helse.ui.onboarding

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.R

class PushSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.push_settings, rootKey)
    }

    companion object {
        fun newInstance() = PushSettingsFragment()
    }
}