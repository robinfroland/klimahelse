package com.example.helse

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.helse.airquality.AirqualityActivity
import com.example.helse.airquality.AirqualityForecast
import com.example.helse.airquality.AirqualityLocation
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        open_airquality.setOnClickListener {
            startActivity(Intent(this, AirqualityActivity::class.java))
        }

    }
}

object LocationStringBuilder {
    fun buildString(airqualityLocations: List<AirqualityLocation>): String {
        val locationString = StringBuilder().append("Locations:\n")
        airqualityLocations.forEach { locationString.append(it.name + ", " + it.kommune + "\n" + ", " + it.station + "\n") }
        return locationString.toString()
    }
}
