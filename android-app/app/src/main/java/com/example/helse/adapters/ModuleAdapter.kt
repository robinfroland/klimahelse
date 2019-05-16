package com.example.helse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.helse.R
import com.example.helse.data.entities.Module
import com.example.helse.utilities.*
import kotlinx.android.synthetic.main.list_item_module.view.*

class ModuleAdapter(private var enabledModules: ArrayList<Module>) : RecyclerView.Adapter<ModuleViewHolder>() {
    private lateinit var preferences: Preferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        preferences = Injector.getAppPreferences(parent.context)
        val layoutInflater = LayoutInflater.from(parent.context)
        val moduleLayout = layoutInflater.inflate(R.layout.list_item_module, parent, false)
        return ModuleViewHolder(moduleLayout)
    }

    override fun getItemCount(): Int {
        return enabledModules.size
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val moduleKey = enabledModules[position].moduleKey
        val moduleIcon = enabledModules[position].iconResourceId
        val category = enabledModules[position].category
        val value = enabledModules[position].dangerIndicator

        holder.module.module_icon.setImageResource(moduleIcon)
        holder.module.module_title.text = category
        holder.module.value_label.text = value

        // Set correct risk label
        when(value) {
            LOW_VALUE -> holder.module.value_label.setBackgroundResource(R.drawable.indicator_danger_low)
            MEDIUM_VALUE -> holder.module.value_label.setBackgroundResource(R.drawable.indicator_danger_medium)
            HIGH_VALUE -> holder.module.value_label.setBackgroundResource(R.drawable.indicator_danger_very_high)
            VERY_HIGH_VALUE -> holder.module.value_label.setBackgroundResource(R.drawable.indicator_danger_very_high)
            LOW_HUMIDITY_VALUE -> holder.module.value_label.setBackgroundResource(R.drawable.indicator_danger_medium)
            GOOD_HUMIDITY_VALUE -> holder.module.value_label.setBackgroundResource(R.drawable.indicator_danger_low)
            HIGH_HUMIDITY_VALUE -> holder.module.value_label.setBackgroundResource(R.drawable.indicator_danger_medium)
        }

        if (preferences.isNotificationEnabled(enabledModules[position])) {
            holder.module.push_btn.setImageResource(R.drawable.ic_notifications_enabled)
        } else {
            holder.module.push_btn.setImageResource(R.drawable.ic_notifications_disabled)
        }

        holder.module.card_module.setOnClickListener {
            when (moduleKey) {
                AIRQUALITY_MODULE -> Navigation.findNavController(it).navigate(R.id.dashboard_to_airquality)
                UV_MODULE -> Navigation.findNavController(it).navigate(R.id.dashboard_to_uv)
                HUMIDITY_MODULE -> Navigation.findNavController(it).navigate(R.id.dashboard_to_humidity)
            }
        }

        holder.module.push_btn.setOnClickListener {
            when (preferences.isNotificationEnabled(enabledModules[position])) {
                true -> {
                    preferences.enableNotifications(enabledModules[position], false)
                    holder.module.push_btn.setImageResource(R.drawable.ic_notifications_disabled)
                }
                false -> {
                    holder.module.push_btn.setImageResource(R.drawable.ic_notifications_enabled)
                    preferences.enableNotifications(enabledModules[position], true)
                }
            }
        }
    }
}

class ModuleViewHolder(val module: View) : RecyclerView.ViewHolder(module)