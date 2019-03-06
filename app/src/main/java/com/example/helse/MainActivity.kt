package com.example.helse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.helse.forecast.AirqualityForecast
import com.example.helse.forecast.AirqualityForecastApiImpl
import com.example.helse.forecast.AirqualityForecastRepositoryImpl
import com.example.helse.forecast.AirqualityForecastViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var notificationService: NotificationService
    private lateinit var text: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationService = NotificationService(this)

        val viewModel = ViewModelProviders.of(this).get(AirqualityForecastViewModel::class.java)
            .apply { airqualityForecastRepository = AirqualityForecastRepositoryImpl(AirqualityForecastApiImpl()) }

        viewModel.getAirqualityForecast().observe(
            this,
            Observer { airqualityForecast -> text = ForecastStringBuilder.buildString(airqualityForecast) })
        print("apiResponse: $text")
    }

    override fun onResume() {
        super.onResume()
        notificationService.sendNotification(NotificationService.NotificationTypes.Resume)
    }

    override fun onPause() {
        super.onPause()
        notificationService.sendNotification(NotificationService.NotificationTypes.Pause)
    }

}

object ForecastStringBuilder {
    fun buildString(forecast: List<AirqualityForecast>): String {
        val forecastString = StringBuilder().append("Forecast:\n")
        forecast.forEach { forecastString.append(/*Things of the api..*/"hi") }
        return forecastString.toString()
    }
}
