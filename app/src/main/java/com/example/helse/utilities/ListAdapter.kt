package com.example.helse.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import com.example.helse.R
import com.example.helse.databinding.ActivityRowitemBinding
import kotlinx.android.synthetic.main.activity_rowitem.view.*


class ListAdapter(internal var data: List<String>, context: Context) : BaseAdapter(), Filterable {
    private var stringFilterList: List<String> = data
    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var filter = ValueFilter()

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
        /* View adapter is built such that instead of creating a new item for each item, views that scroll
        off screen can be reused. Therefore we create a new view if it is null, else we return the already
        created view
         */
        val rowItem = if (convertView == null) {
            val rowItemBinding: ActivityRowitemBinding =
                DataBindingUtil.inflate(inflater, R.layout.activity_rowitem, parent, false)
            rowItemBinding.root
        } else {
            convertView
        }

        rowItem.title.text = data[position]
        return rowItem
    }

    override fun getFilter(): Filter {
        return filter
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

        // Kotlin is not smart enough to understand the type is checked, so we need to suppress it
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            val values = results.values
            // Type arguments are erased when checking for types, which is why * is used instead of String,
            // and why there is a dedicated method for deeply inspecting the types
            if (values is ArrayList<*> && values.itemsAreOfType<String>() != null) {
                data = values as ArrayList<String>
            }
            notifyDataSetChanged()
        }
    }
}

