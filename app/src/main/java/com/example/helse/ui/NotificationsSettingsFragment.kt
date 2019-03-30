package com.example.helse.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.helse.R
import com.example.helse.utilities.ENABLE_NOTIFICATIONS_FROM
import com.example.helse.utilities.ENABLE_NOTIFICATIONS_TO
import kotlinx.android.synthetic.main.fragment_notifications_settings.*

class NotificationsSettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_notifications_settings, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickerNotificationsFrom.setIs24HourView(true)
        pickerNotificationsFrom.setOnTimeChangedListener { _, hourOfDay, minute ->
            updatePreferences(ENABLE_NOTIFICATIONS_FROM, hourOfDay, minute)
        }

        pickerNotificationsTo.setIs24HourView(true)
        pickerNotificationsTo.setOnTimeChangedListener { _, hourOfDay, minute ->
            updatePreferences(ENABLE_NOTIFICATIONS_TO, hourOfDay, minute)
        }
    }

    private fun updatePreferences(name: String, hour: Int, minute: Int) {
        activity!!.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString("hour", hour.toString())
            .apply()
        activity!!.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString("minute", minute.toString())
            .apply()
    }
}