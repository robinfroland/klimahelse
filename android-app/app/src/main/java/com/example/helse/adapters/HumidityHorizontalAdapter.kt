package com.example.helse.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.helse.R
import com.example.helse.data.entities.HumidityForecast
import com.example.helse.ui.humidity.HumidityFragment
import com.example.helse.utilities.GOOD_HUMIDITY_VALUE
import com.example.helse.utilities.HIGH_HUMIDITY_VALUE
import com.example.helse.utilities.LOW_HUMIDITY_VALUE
import kotlinx.android.synthetic.main.list_item_risk.view.*

class HumidityHorizontalAdapter(private val timeList: MutableList<HumidityForecast>, private val humidityFragment: HumidityFragment) :
    RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val riskCircleLayout = layoutInflater.inflate(R.layout.list_item_risk, parent, false) as CardView
        return CardViewHolder(riskCircleLayout)
    }

    override fun onBindViewHolder(holder: CardViewHolder, index: Int) {

        holder.view.horizontal_item.risk_text.text = timeList[index].from.subSequence(11, 16)

        when (timeList[index].riskValue) {
            LOW_HUMIDITY_VALUE -> holder.view.horizontal_item.risk_circle.setBackgroundResource(R.drawable.circle_humidity_low)
            GOOD_HUMIDITY_VALUE -> holder.view.horizontal_item.risk_circle.setBackgroundResource(R.drawable.circle_humidity_ok)
            HIGH_HUMIDITY_VALUE -> holder.view.horizontal_item.risk_circle.setBackgroundResource(R.drawable.circle_humidity_high)
        }

        holder.view.risk_circle.setOnClickListener {
            humidityFragment.setSliderToChosenTime(timeList[index], index)
        }
    }

    override fun getItemCount(): Int {
        return timeList.size
    }
}
