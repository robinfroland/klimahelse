package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    val locations = ArrayList<Location>()
    private val url = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchData(url)
    }

    fun fetchData(url: String){
        val response= LocationApiRequest().execute(url).get()
        val jObject = JSONArray(response)

        for (i in 0 until jObject.length()) {
            val jobject = jObject.getJSONObject(i)
            val name = jobject.getString("name")
            val kommuneName = jobject.getJSONObject("kommune").getString("name")
            locations.add(Location(name, kommuneName))
        }

        text.text = LocationStringBuilder.buildString(locations)
    }

    object LocationStringBuilder {
        fun buildString(locations: List<Location>): String {
            val locationString = StringBuilder().append("Locations:\n")
            locations.forEach { locationString.append(it.name + ", " + it.kommune + "\n") }
            return locationString.toString()
        }
    }

}
