package com.example.helse.utilities

import android.annotation.SuppressLint
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

class ListAdapter(internal var mData: List<String>) : BaseAdapter(), Filterable {
    internal var mStringFilterList: List<String>
    internal var valueFilter: ValueFilter? = null
    private var inflater: LayoutInflater? = null

    init {
        mStringFilterList = mData
    }


    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): String {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if (inflater == null) {
            inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        val rowItemBinding: ActivityRowitemBinding = DataBindingUtil.inflate(inflater!!, R.layout.activity_rowitem, parent, false)
        rowItemBinding.stringName.setText(mData[position])


        return rowItemBinding.getRoot()
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as ValueFilter
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {
                val filterList = ArrayList<String>()
                for (i in mStringFilterList.indices) {
                    if (mStringFilterList[i].toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList[i])
                    }
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = mStringFilterList.size
                results.values = mStringFilterList
            }
            return results

        }

        override fun publishResults(
            constraint: CharSequence,
            results: Filter.FilterResults
        ) {
            mData = results.values as ArrayList<String>
            notifyDataSetChanged()
        }

    }

}