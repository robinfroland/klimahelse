package com.example.helse.data.api

import android.util.Xml
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.entities.emptyUvForecast
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.xmlpull.v1.XmlPullParser

object UvForecastApi {
    private val client = OkHttpClient()
    private val preferences: Preferences = Injector.getAppPreferences(AppContext.getAppContext())
    private val location: Location = preferences.getLocation()

    fun fetchUv(): MutableList<UvForecast> {
        lateinit var response: Response
        return try {
            val uri = fetchUvURI()
            // Get URI's for today, tomorrow, and overtomorrow
            val request = Request.Builder()
                .url(uri)
                .build()

            response = client.newCall(request).execute()

            response.parseUvResponse(location)
        } catch (e: Exception) {
            mutableListOf(emptyUvForecast)
        }
    }


    // Fetch URI's from endpoint. Return URI for today.
    // Tomorrow and day after tomorrow available later if wanted(just return the list instead of index 0
    private fun fetchUvURI(): String {
        val client = OkHttpClient()
        // Get URI's for today, tomorrow, and overtomorrow
        val request = Request.Builder()
            .url("https://api.met.no/weatherapi/uvforecast/1.0/available")
            .build()
        val response = client.newCall(request).execute()
        response.body()?.byteStream()
            .use { inputStream ->
                val parser: XmlPullParser = Xml.newPullParser()
                    .apply { setInput(inputStream, null) }
                val uris = arrayListOf<String>()
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    if (parser.name == "uri") {
                        // Get next element to get the uri value
                        parser.next()
                        val uri = parser.text
                        if (uri.contains("content_type=text")) {
                            uris.add(uri)
                        }
                    }
                }
                return uris[0]
            }
    }
}

