package com.example.helse.search

import android.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.widget.ListAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.helse.R
import com.example.helse.search.databinding.ActivityMainBinding

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    internal lateinit var activityMainBinding: ActivityMainBinding
    internal lateinit var adapter: ListAdapter

    internal var searchList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = setContentView(this, R.layout.activity_main)

        searchList.add("Oslo")
        searchList.add("Bergen")
        searchList.add("Stavanger")
        searchList.add("Trondheim")

        adapter = ListAdapter(searchList)
        activityMainBinding.listView.setAdapter(adapter)

        activityMainBinding.search.setActivated(true)
        activityMainBinding.search.setQueryHint("Søk etter område")
        activityMainBinding.search.onActionViewExpanded()
        activityMainBinding.search.setIconified(false)
        activityMainBinding.search.clearFocus()

        activityMainBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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