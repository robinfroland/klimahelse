package com.example.helse.ui.settings

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.R
import com.example.helse.utilities.Injector

class PushSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.push_settings, rootKey)
    }

    companion object {
        fun newInstance() = PushSettingsFragment()
    }
}