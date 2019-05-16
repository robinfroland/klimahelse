package com.example.helse.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.helse.R
import com.example.helse.data.entities.RiskCircles
import com.example.helse.ui.airquality.AirqualityFragment
import com.example.helse.utilities.HIGH_VALUE
import com.example.helse.utilities.LOW_VALUE
import com.example.helse.utilities.MEDIUM_VALUE
import com.example.helse.utilities.VERY_HIGH_VALUE
import kotlinx.android.synthetic.main.list_item_risk.view.*

class HorisontalAdapter(private val timeList: MutableList<RiskCircles>, private val fragment: AirqualityFragment) :
    RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val riskCircleLayout = layoutInflater.inflate(R.layout.list_item_risk, parent, false) as CardView
        return CardViewHolder(riskCircleLayout)
    }

    override fun onBindViewHolder(holder: CardViewHolder, index: Int) {
        val hourOfDay = timeList[index].hourOfDay.toString()
        val riskValue = timeList[index].overallRiskValue
        holder.view.horizontal_item.risk_text.text = ("$hourOfDay:00")

        when (riskValue) {
            LOW_VALUE -> holder.view.horizontal_item.risk_circle.setBackgroundResource(R.drawable.circle_danger_low)
            MEDIUM_VALUE -> holder.view.horizontal_item.risk_circle.setBackgroundResource(R.drawable.circle_danger_medium)
            HIGH_VALUE -> holder.view.horizontal_item.risk_circle.setBackgroundResource(R.drawable.circle_danger_high)
            VERY_HIGH_VALUE -> holder.view.horizontal_item.risk_circle.setBackgroundResource(R.drawable.circle_danger_very_high)
        }

        holder.view.risk_circle.setOnClickListener {
            fragment.setScreenToChosenTime(index)
        }
    }

    override fun getItemCount(): Int {
        return timeList.size
    }
}

class CardViewHolder(val view: CardView) : RecyclerView.ViewHolder(view)