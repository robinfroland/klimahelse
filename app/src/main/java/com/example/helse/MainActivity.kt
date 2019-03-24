package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.helse.ui.onboarding.OnboardingActivity
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.setupErrorHandling
import kotlinx.android.synthetic.main.activity_main.*

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