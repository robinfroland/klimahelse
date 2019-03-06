package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}

object LocationStringBuilder {
    fun buildString(locations: List<Location>): String {
        val locationString = StringBuilder().append("Locations:\n")
        locations.forEach { locationString.append(it.name + " " + it.kommune + "\n") }
        return locationString.toString()
    }
}