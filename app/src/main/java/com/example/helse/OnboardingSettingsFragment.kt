package com.example.helse


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.activity_onboarding.view.*
import kotlinx.android.synthetic.main.fragment_onboarding_settings.view.*


class OnboardingSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_onboarding_settings, container, false)

        view.finishBtn.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            // destroy onboarding lifecycle
        }
        return view
    }

    companion object {
        fun newInstance() = OnboardingSettingsFragment()
    }


}
