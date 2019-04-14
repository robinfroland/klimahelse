package com.example.helse

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupErrorHandling(intent, this)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottom_navbar.setupWithNavController(navController)
        bottom_navbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent))

        NavigationUI.setupActionBarWithNavController(this, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label != "Dashboard") {
                toolbar_title.visibility = View.VISIBLE
                toolbar_title.text = destination.label
            } else {
                toolbar_title.visibility = View.INVISIBLE
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
            "push_advanced_settings" -> navController.navigate(R.id.pushsettings_to_advanced)
            "location_search_settings" -> navController.navigate(R.id.locationsettings_to_search)
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