package com.example.helse.ui.onboarding


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.example.helse.R
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.Preferences
import kotlinx.android.synthetic.main.fragment_onboarding.view.*

class OnboardingFragment : Fragment() {

    private lateinit var onboardingPager: ViewPager
    private lateinit var setupBtn: Button
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)
        preferences = AppPreferences(requireContext())

        onboardingPager = requireActivity().findViewById(R.id.viewPager)
        setupBtn = view.setupBtn

        setupBtn.setOnClickListener {
            onboardingPager.currentItem = 1
        }

        return view
    }

    companion object {
        fun newInstance() = OnboardingFragment()

    }
}
