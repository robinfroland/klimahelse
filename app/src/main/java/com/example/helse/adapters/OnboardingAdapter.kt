package com.example.helse.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.helse.OnboardingFragment
import com.example.helse.OnboardingSettingsFragment

class OnboardingAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(fragmentNumber: Int): Fragment {
        return when(fragmentNumber) {
            0 -> OnboardingFragment.newInstance()
            1 -> OnboardingSettingsFragment.newInstance()
            else -> OnboardingFragment.newInstance()
        }
    }

    override fun getCount() = 2
}