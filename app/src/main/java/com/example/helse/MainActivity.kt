package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.setupErrorHandling

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupErrorHandling(intent, this)

        val pref = AppPreferences(this)
        pref.setFirstLaunch(true)
    }
}
