package com.example.helse

import okhttp3.Request
import android.os.AsyncTask
import okhttp3.OkHttpClient
import java.io.IOException

class LocationApiRequest : AsyncTask<String, Void, String>() {

    private var client = OkHttpClient()

    override fun doInBackground(vararg urls: String): String? {
        var retValue: String? = null
        try {
            val request = Request.Builder()
                .url(urls[0])
                .get()
                .build()

            val response = client.newCall(request).execute()
            retValue = response.body()!!.string()
            
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return retValue
    }
}
