package com.example.helse


import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.helse.databinding.ActivitySearchBinding
import com.example.helse.utilities.ListAdapter
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var activitySearch: ActivitySearchBinding
    internal lateinit var adapter: ListAdapter
    private var searchList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearch = DataBindingUtil.setContentView(this, R.layout.activity_search)

        searchList.add("Oslo")
        searchList.add("Bergen")
        searchList.add("Stavanger")
        searchList.add("Trondheim")

        adapter = ListAdapter(searchList, this)
        activitySearch.listView.adapter = adapter

        activitySearch.search.isActivated = true
        activitySearch.search.queryHint = "Søk etter område"
        activitySearch.search.onActionViewExpanded()
        activitySearch.search.isIconified = false
        activitySearch.search.clearFocus()

        activitySearch.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)

                return false
            }
        })
    }
}