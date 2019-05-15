package com.example.helse.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.helse.ui.onboarding.OnboardingFragment
import com.example.helse.ui.settings.DashboardSettingsFragment
import com.example.helse.ui.settings.LocationSettingsFragment
import com.example.helse.ui.settings.PushSettingsFragment
import com.example.helse.ui.settings.SettingsFragment

@Suppress("DEPRECATION")
class OnboardingAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(fragmentNumber: Int): Fragment {
        return when(fragmentNumber) {
            0 -> OnboardingFragment.newInstance()
            1 -> DashboardSettingsFragment.newInstance()
            2 -> PushSettingsFragment.newInstance()
            3 -> LocationSettingsFragment.newInstance()
            else -> OnboardingFragment.newInstance()
        }
    }

    override fun getCount() = 4
}