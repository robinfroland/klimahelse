package com.example.helse

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.helse.ui.onboarding.OnboardingActivity
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.UpdateAirqualityWorker
import com.example.helse.utilities.setupErrorHandling
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

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


    private fun setFirstLaunch() {
        val preferences = AppPreferences(this)

        if (preferences.isFirstLaunch()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            preferences.setFirstLaunch(false) // set true to test onboarding
            finish()
        }
    }
}