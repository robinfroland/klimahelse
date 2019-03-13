package com.example.helse


import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.helse.data.api.LocationApi
import com.example.helse.data.database.LocationDao
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.LocationRepository
import com.example.helse.databinding.ActivitySearchBinding
import com.example.helse.utilities.SearchListAdapter
import com.example.helse.viewmodels.SearchViewModel
import java.util.*

class SearchActivity : AppCompatActivity() {

    private lateinit var locationApi: LocationApi
    lateinit var dao: LocationDao
    private lateinit var activitySearch: ActivitySearchBinding
    internal lateinit var adapter: SearchListAdapter
    private var searchList: MutableList<String> = ArrayList()
    private lateinit var locationList: List<Location>
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        activitySearch = DataBindingUtil.setContentView(this, R.layout.activity_search)

        searchList.add("Oslo")
        searchList.add("Bergen")
        searchList.add("Stavanger")
        searchList.add("Trondheim")

        adapter = SearchListAdapter(searchList, this)
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

    private fun initViewModel() {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
            .apply {
                locationRepository = LocationRepository(
                    dao,
                    locationApi
                )
            }
    }
}