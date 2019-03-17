package com.example.helse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.helse.utilities.AppPreferences
import com.example.helse.utilities.Preferences

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = AppPreferences(this)
        pref.setFirstLaunch(true)
    }
}
