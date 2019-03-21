package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.setupErrorHandling
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setFirstLaunch()
        setTheme(R.style.Theme_MyApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupErrorHandling(intent, this)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottom_navbar.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)
//        button.setOnClickListener {
//            startActivity(Intent(this, AirqualityActivity::class.java))
//        }
//
//        setupErrorHandling(intent, this)
//
//        open_settings_button.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .add(R.id.main, NotificationsSettingsFragment()).commit()
//        }
    }

    private fun setFirstLaunch() {
        val preferences = AppPreferences(this)

        if (preferences.isFirstLaunch()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            preferences.setFirstLaunch(false) // comment out code to test onboardingprocess
            finish()
        }
    }
}