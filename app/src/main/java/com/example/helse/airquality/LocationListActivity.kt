package com.example.helse.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.helse.R

class LocationListActivity : AppCompatActivity() {

    private lateinit var locationArray: ArrayList<AirqualityLocation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        locationArray = arrayListOf(AirqualityLocation("Oslo", "Alnabru", "NO0057A"),
            AirqualityLocation("Oslo", "Majorstuen", "NO0057A"))
    }
}
