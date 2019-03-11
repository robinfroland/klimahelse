package com.example.helse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.adapters.LocationListAdapter
import com.example.helse.data.entity.AirqualityLocation
import kotlinx.android.synthetic.main.activity_location_list.*


class LocationListActivity : AppCompatActivity() {

    private lateinit var locationArray: ArrayList<AirqualityLocation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        locationArray = arrayListOf(
            AirqualityLocation("Alnabru", "Oslo", "NO0057A"),
            AirqualityLocation("Majorstuen", "Oslo", "NO0057A")
        )

        recyclerView_locations.layoutManager = LinearLayoutManager(this)
        recyclerView_locations.adapter = LocationListAdapter(locationArray)


    }
}
