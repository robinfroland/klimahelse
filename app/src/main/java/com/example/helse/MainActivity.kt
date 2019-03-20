package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.helse.utilities.setupErrorHandling
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupErrorHandling(intent, this)

        open_settings_button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.main, NotificationsSettingsFragment()).commit()
        }
    }
}