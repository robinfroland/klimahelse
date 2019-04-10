package com.example.helse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.helse.R
import com.example.helse.data.entities.ModuleCard
import kotlinx.android.synthetic.main.list_item_module.view.*

class ModuleAdapter(private var enabledModules: ArrayList<ModuleCard>) : RecyclerView.Adapter<ModuleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val moduleLayout = layoutInflater.inflate(R.layout.list_item_module, parent, false)
        return ModuleViewHolder(moduleLayout)
    }

    override fun getItemCount(): Int {
        return enabledModules.size
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val iconResourceId = enabledModules[position].iconResourceId
        val category = enabledModules[position].category
        val dangerIndicator = enabledModules[position].dangerIndicator
        val pushEnabled = enabledModules[position].pushEnabled
        holder.module.module_icon.setImageResource(iconResourceId)
        holder.module.module_title.text = category
        holder.module.module_dangerindicator.text = dangerIndicator

        when (dangerIndicator) {
            "HIGH" -> {
                holder.module.module_dangerindicator.setBackgroundResource(R.drawable.indicator_danger_high)
                holder.module.module_dangerindicator.text = "HØY RISIKO"
            }
            "MEDIUM" -> {
                holder.module.module_dangerindicator.setBackgroundResource(R.drawable.indicator_danger_medium)
                holder.module.module_dangerindicator.text = "MIDDELS RISIKO"
            }
            "LOW" -> {
                holder.module.module_dangerindicator.setBackgroundResource(R.drawable.indicator_danger_low)
                holder.module.module_dangerindicator.text = "LAV RISIKO"
            }
        }

        if (pushEnabled) {
            holder.module.module_push.setImageResource(R.drawable.ic_notifications_enabled)
        } else {
            holder.module.module_push.setImageResource(R.drawable.ic_notifications_disabled)
        }

        holder.module.card_module.setOnClickListener {
            when (category.toLowerCase()) {
                "luftkvalitet" -> Navigation.findNavController(it).navigate(R.id.dashboard_to_airquality)
                "uv-stråling" -> Navigation.findNavController(it).navigate(R.id.dashboard_to_uv)
                "luftfuktighet" -> Navigation.findNavController(it).navigate(R.id.dashboard_to_humidity)
            }
        }
    }
}

class ModuleViewHolder(val module: View) : RecyclerView.ViewHolder(module)