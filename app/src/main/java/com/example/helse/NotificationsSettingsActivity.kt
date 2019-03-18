package com.example.helse

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notifications_settings.*

class NotificationsSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_settings)

        pickerNotificationsFrom.setIs24HourView(true)
        pickerNotificationsFrom.setOnTimeChangedListener { _, hourOfDay, minute ->
            updatePreferences("notificationsFrom", hourOfDay, minute)
        }

        pickerNotificationsTo.setIs24HourView(true)
        pickerNotificationsTo.setOnTimeChangedListener { _, hourOfDay, minute ->
            updatePreferences("notificationsTo", hourOfDay, minute)
        }
    }

    private fun updatePreferences(name: String, hour: Int, minute: Int) {
        getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString("hour", hour.toString())
            .apply()
        getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString("minute", minute.toString())
            .apply()
    }
}