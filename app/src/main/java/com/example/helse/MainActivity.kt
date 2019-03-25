package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
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
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setFirstLaunch()
        setContentView(R.layout.activity_main)

        setupErrorHandling(intent, this)

        val constraints = Constraints.Builder().setRequiresCharging(true).build()
        val saveRequest = PeriodicWorkRequestBuilder<UpdateAirqualityWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance().enqueue(saveRequest)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setSupportActionBar(toolbar)

        bottom_navbar.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)

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