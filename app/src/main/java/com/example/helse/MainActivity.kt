package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.forecast.AirqualityForecast
import com.example.helse.forecast.AirqualityForecastApiImpl
import com.example.helse.forecast.AirqualityForecastRepositoryImpl
import com.example.helse.forecast.AirqualityForecastViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var text: String
    private val url = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"
    val locations = ArrayList<Location>()

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

    text = "temp"

    val viewModel = ViewModelProviders.of(this).get(AirqualityForecastViewModel::class.java).apply { airqualityForecastRepository = AirqualityForecastRepositoryImpl(AirqualityForecastApiImpl()) }

    viewModel.getAirqualityForecast().observe(this, Observer { airqualityForecast ->
            findViewById<TextView>(R.id.text).text = ForecastStringBuilder.buildString(airqualityForecast)})
    println("apiResponse: ${findViewById<TextView>(R.id.text).text}")
}


object ForecastStringBuilder {
    fun buildString(forecast: List<AirqualityForecast>): String {
        val forecastString = StringBuilder().append("Forecast:\n")
        forecast.forEach {
            forecastString.append(
                "\n${it.location.kommune}, ${it.location.name}\n" +
                        "${it.location.station}\n" +
                        "${it.Airquality.from}\n" +
                        "${it.Airquality.to}\n" +
                        "o3_concentration: ${it.Airquality.variables.o3_concentration}\n" +
                        "no2_concentration: ${it.Airquality.variables.no2_concentration}\n" +
                        "pm10_concentration: ${it.Airquality.variables.pm10_concentration}\n" +
                        "pm25_concentration: ${it.Airquality.variables.pm25_concentration}\n"
            )
        }
        return forecastString.toString()
    }
}
