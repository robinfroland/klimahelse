package com.example.helse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.helse.data.entity.Location
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
    fun buildString(airqualityLocations: List<Location>): String {
        val locationString = StringBuilder().append("Locations:\n")
        airqualityLocations.forEach { locationString.append(it.superlocation + ", " + it.location + "\n" + ", " + it.stationID + "\n") }
        return locationString.toString()
    }
}
