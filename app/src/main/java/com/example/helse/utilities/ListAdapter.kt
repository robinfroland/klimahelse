package com.example.helse.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import java.util.ArrayList
import androidx.databinding.DataBindingUtil
import com.example.helse.R
import com.example.helse.databinding.ActivityRowitemBinding




class ListAdapter(internal var data: List<String>) : BaseAdapter(), Filterable {
    private var stringFilterList: List<String> = data
    private var inflater: LayoutInflater? = null

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): String {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if (inflater == null) {
            inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        val rowItemBinding: ActivityRowitemBinding = DataBindingUtil.inflate(inflater!!, R.layout.activity_rowitem, parent, false)
        rowItemBinding.stringName.text = data[position]

        return rowItemBinding.root
    }

    override fun getFilter(): Filter {
        return ValueFilter()
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()

            if (!constraint.isNullOrEmpty()) {
                val filterList = ArrayList<String>()
                for (i in stringFilterList.indices) {
                    if (stringFilterList[i].toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filterList.add(stringFilterList[i])
                    }
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = stringFilterList.size
                results.values = stringFilterList
            }
            return results

        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            data = results.values as ArrayList<String>
            notifyDataSetChanged()
        }

    }

}