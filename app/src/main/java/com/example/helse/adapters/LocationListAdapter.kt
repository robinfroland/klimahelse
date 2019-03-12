package com.example.helse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helse.R
import com.example.helse.data.entities.Location
import kotlinx.android.synthetic.main.location_element.view.*

class LocationListAdapter(
    private var locations: List<Location>
) : RecyclerView.Adapter<ListViewHolder>() {

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val locationRow = layoutInflater.inflate(R.layout.location_element, parent, false)
        return ListViewHolder(locationRow)
    }

    override fun onBindViewHolder(holder: ListViewHolder, index: Int) {
        val location = locations[index].location
        val superlocation = locations[index].superlocation
        holder.view.location.text = location
        holder.view.superlocation.text = superlocation
    }

}

class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view)