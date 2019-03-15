package com.example.helse


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_onboarding_settings.view.*


class OnboardingSettingsFragment : Fragment() {

    private lateinit var finishOnboarding: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_onboarding_settings, container, false)

        finishOnboarding = view.finishOnboardingBtn

        finishOnboarding.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }
        return view
    }

    companion object {
        fun newInstance() = OnboardingSettingsFragment()
    }


}
