package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.adapters.LocationListAdapter
import com.example.helse.data.api.LocationResponse
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.repositories.LocationRepository
import com.example.helse.viewmodels.LocationViewModel
import kotlinx.android.synthetic.main.activity_location_list.*


class LocationListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        val locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
            .apply {
                locations = LocationRepository(
                    LocalDatabase.getInstance(applicationContext).locationDao(),
                    LocationResponse()
                )
            }

        locationViewModel.getAllLocations().observe(this, Observer { locations ->
            recyclerView_locations.layoutManager = LinearLayoutManager(this)
            recyclerView_locations.adapter = LocationListAdapter(locations)
        })

    }
}
