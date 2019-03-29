package com.example.helse.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.helse.R
import kotlinx.android.synthetic.main.list_item_risk.view.*


class HorisontalAdapter(private val timeList : ArrayList<Int>) : RecyclerView.Adapter<CardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val riskCircleLayout = layoutInflater.inflate(R.layout.list_item_risk, parent, false) as CardView
        return CardViewHolder(riskCircleLayout)
    }

    override fun onBindViewHolder(holder: CardViewHolder, index: Int) {
        holder.view.risk_circle.risk_text.text = timeList[index].toString()
    }

    override fun getItemCount(): Int {
        return 24
    }


}

class CardViewHolder(val view: CardView) : RecyclerView.ViewHolder(view)



