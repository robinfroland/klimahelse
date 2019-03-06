package com.example.helse

import android.util.Xml
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser


interface LocationApiInterface {
    fun fetchLocations(): List<Location>
}

class LocationApiRequest: LocationApiInterface {

    private val client = OkHttpClient()
    private val url = "https://api.met.no/weatherapi/airqualityforecast/0.1/stations"


    override fun fetchLocations(): List<Location> {
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        val locations = ArrayList<Location>()

        response.body()?.byteStream()
            .use { inputStream ->
                val parser: XmlPullParser = Xml.newPullParser()
                    .apply { setInput(inputStream, null) }

                var currentLocation: Location? = null

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }

                    if (parser.name == "location") {
                        currentLocation = Location(parser.getAttributeValue(1))
                        locations.add(currentLocation)
                    } else if (parser.name == "forest-fire") {
                        currentLocation?.airQuality = parser.getAttributeValue(1)
                    }
                }
            }
        return locations
    }

}
