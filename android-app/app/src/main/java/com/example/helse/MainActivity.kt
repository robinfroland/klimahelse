package com.example.helse

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.utilities.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        setFirstLaunch()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)
        subscribePushNotification()
        setupNavBars()
        updateDeviceLocation()
    }

    private fun updateDeviceLocation() {
        val preferences = Injector.getAppPreferences(applicationContext)
        val deviceLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (preferences.locationPermissionGranted()) {
            try {
                deviceLocationClient.lastLocation.addOnSuccessListener {
                    if (it != null && it.latitude != 0.0 && it.longitude != 0.0) {
                        preferences.setDeviceLocation(it.latitude, it.longitude)
                    } else {
                        getString(R.string.failed_location_query).toast(applicationContext)
                    }
                }
            } catch (e: SecurityException) {
                println("setDeviceLocation() failed with exception $e")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat?, pref: Preference?): Boolean {
        when (pref?.key) {
            LOCATION_SETTINGS -> navController.navigate(R.id.settings_to_search)
        }
        return true
    }

    private fun setupNavBars() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottom_navbar.setupWithNavController(navController)
        setupActionBarWithNavController(navController, null)

        setupDynamicToolbarUi()
    }

    private fun setFirstLaunch() {
        val preferences = Injector.getAppPreferences(this)
        if (preferences.isFirstLaunch()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }
    }

    private fun subscribePushNotification() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("NOT SUCCESSFUL")
                return@OnCompleteListener
            }
        })
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
    }

    private fun setupDynamicToolbarUi() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Change toolbar and colors according to fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.setBackgroundColor(Color.TRANSPARENT)
            toolbar.navigationIcon?.setColorFilter(
                resources.getColor(R.color.colorGreyDark, null), PorterDuff.Mode.SRC_ATOP
            )
            window.statusBarColor = Color.TRANSPARENT
            toolbar_title.text = ""

            when (destination.label) {
                SETTINGS_LABEL -> {
                    toolbar.setBackgroundColor(Color.WHITE)
                    window.statusBarColor = Color.WHITE
                    toolbar_title.text = SETTINGS_LABEL
                    toolbar_title.setTextColor(resources.getColor(R.color.colorGreyDark, null))
                }
                MAP_LABEL -> {
                    toolbar.navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }
            }
        }
    }
}
