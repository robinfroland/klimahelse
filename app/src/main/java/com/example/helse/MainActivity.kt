package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.helse.ui.onboarding.OnboardingActivity
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.setupErrorHandling
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setFirstLaunch()
        setContentView(R.layout.activity_main)

        setupErrorHandling(intent, this)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottom_navbar.setupWithNavController(navController)
        bottom_navbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent))

        NavigationUI.setupActionBarWithNavController(this, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label != "DashboardFragment") {
                supportActionBar?.setDisplayShowTitleEnabled(true)
            } else {
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat?, pref: Preference?): Boolean {
        when(pref?.key) {
            "location_settings" -> navController.navigate(R.id.settings_to_locationsettings)
            "dashboard_settings" -> navController.navigate(R.id.settings_to_dashboardsettings)
            "push_settings" -> navController.navigate(R.id.settings_to_pushsettings)
        }
        return true
    }


    private fun setFirstLaunch() {
        PreferenceManager.setDefaultValues(this, R.xml.root_settings, false)
        val preferences = AppPreferences(this)

        if (preferences.isFirstLaunch()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            preferences.setFirstLaunch(false) // set true to test onboarding
            finish()
        }
    }
}