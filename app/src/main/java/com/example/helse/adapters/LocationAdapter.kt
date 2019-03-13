package com.example.helse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.helse.R
import com.example.helse.data.entities.Location
import kotlinx.android.synthetic.main.list_item_location.view.*
//private var locations: MutableList<Location>
class LocationAdapter() : RecyclerView.Adapter<LocationViewHolder>(), Filterable {

//    private var locationList = locations
//    private var locationListFull = locationList.toMutableList()
    private lateinit var locationList: MutableList<Location>
    private lateinit var locationListFull: MutableList<Location>
    private var searchFilter = ValueFilter()


    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val locationItem = layoutInflater.inflate(R.layout.list_item_location, parent, false)
        return LocationViewHolder(locationItem)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, index: Int) {
        val location = locationList[index].location
        val superlocation = locationList[index].superlocation
        holder.view.location.text = location
        holder.view.superlocation.text = superlocation
    }

    fun submitList(locations: MutableList<Location>) {
        locationList = locations
        locationListFull = locationList.toMutableList()
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(userInput: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()
            val filteredLocations = mutableListOf<Location>()

            if (userInput.isNullOrEmpty()) {
                filteredLocations.addAll(locationListFull)
            } else {
                val filterPattern = userInput.toString().toLowerCase().trim()
                for (location in locationListFull) {
                    if (location.location.toLowerCase().contains(filterPattern) ||
                        location.superlocation.toLowerCase().contains(filterPattern)
                    )
                        filteredLocations.add(location)
                }
            }
            results.values = filteredLocations
            return results

        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(userInput: CharSequence, results: Filter.FilterResults) {
            locationList.clear()
            locationList = results.values as MutableList<Location>
            notifyDataSetChanged()
        }
    }

}

class LocationViewHolder(val view: View) : RecyclerView.ViewHolder(view)

