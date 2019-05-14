package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.ui.onboarding.OnboardingActivity
import com.example.helse.utilities.*
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
        subscribePushNotification()
        setupNavbars()
//        setupErrorHandling(intent, this)
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

    private fun setupNavbars() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController = findNavController(R.id.nav_host_fragment)
        bottom_navbar.setupWithNavController(navController)
        toolbar.setupWithNavController(navController)
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

            val token = task.result?.token

            println("FIREBASETOKEN $token")
        })

        FirebaseMessaging.getInstance().subscribeToTopic("weather").addOnCompleteListener { task ->
            var msg = "Weather registered successfully"
            if (!task.isSuccessful) {
                msg = "Weather not registered successfully"
            }
            println(msg)
        }
    }

}