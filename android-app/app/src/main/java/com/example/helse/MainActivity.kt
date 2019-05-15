package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
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
        navController = findNavController(R.id.nav_host_fragment)
        subscribePushNotification()
        setupNavigation()
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

    private fun setupNavigation() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottom_navbar.setupWithNavController(navController)
        setupActionBarWithNavController(navController, null)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label != "Innstillinger") {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent))
                toolbar_title.text = ""
            }
        }
    }

    private fun setFirstLaunch() {
        val preferences = Injector.getAppPreferences(this)
        preferences.setFirstLaunch(true)
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