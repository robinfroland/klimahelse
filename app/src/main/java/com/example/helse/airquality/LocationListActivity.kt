package com.example.helse.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helse.R
import kotlinx.android.synthetic.main.activity_location_list.*
import kotlinx.android.synthetic.main.activity_location_list.view.*


class LocationListActivity : AppCompatActivity() {

    private lateinit var locationArray: ArrayList<AirqualityLocation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        locationArray = arrayListOf(AirqualityLocation("Oslo", "Alnabru", "NO0057A"),
            AirqualityLocation("Oslo", "Majorstuen", "NO0057A"))

        recyclerView_locations.layoutManager = LinearLayoutManager(this)
        recyclerView_locations.adapter = LocationListAdapter(locationArray)


    }
}
