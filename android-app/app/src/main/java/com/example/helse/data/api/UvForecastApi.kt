package com.example.helse.data.api

import android.util.Xml
import com.example.helse.data.entities.Location
import com.example.helse.data.entities.UvForecast
import com.example.helse.data.entities.emptyUvForecast
import com.example.helse.utilities.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser

private const val UV_BASE_URL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/uvforecast/1.0/available"

class UvForecastApi : RemoteForecastData<UvForecast> {
    private val client = OkHttpClient()

    override fun fetchForecast(location: Location): MutableList<UvForecast> {
        return try {
            val uri = buildCoordinateURI(location.latitude, location.longitude)
            // Get URI's for today, tomorrow, and overtomorrow
            val request = Request.Builder()
                .url(uri)
                .build()

            val response = client.newCall(request).execute()
            response.parseUvResponse(location)
        } catch (e: Exception) {
            println("fetchUv() failed with exception $e")
            mutableListOf(emptyUvForecast)
        }
    }

    // Fetch URI's from endpoint. Return URI for today.
    // Tomorrow and day after tomorrow available later if wanted(just return the list instead of index 0
    override fun buildCoordinateURI(latitude: Double, longitude: Double): String {
        val client = OkHttpClient()
        // Get URI's for today, tomorrow, and overtomorrow
        val request = Request.Builder()
            .url(UV_BASE_URL)
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


