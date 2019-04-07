package com.example.helse.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.helse.MainActivity
import java.io.PrintWriter
import java.io.StringWriter

class ExceptionHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        val stackTrace = StringWriter()
        if (e == null) {
            throw Error("Omg lol")
        }
        e.printStackTrace(PrintWriter(stackTrace))
        val errorReport = StringBuilder()
        errorReport.append("************ CAUSE OF ERROR ************\n\n")
        errorReport.append(stackTrace.toString())

        val mainIntent = Intent(context, MainActivity::class.java)
        mainIntent.putExtra("error", errorReport.toString())
        context.startActivity(mainIntent)

        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(10)
    }
}

fun showNetworkError(activity: Activity?, responseCode: Int, e: Throwable) {
    if (activity == null) {
        throw Error("showNetworkError(): activity can't be null..\nERROR: ${e.localizedMessage}")
    }
    Log.e("ERROR", e.toString())
    val message = when (responseCode) {
        301, 302, 404, 410 -> "Link does not exist. Please contact the developers."
        500, 501, 502, 503, 504, 505, 506, 507, 508, 510, 511, 429, 420 -> "Server error. Wait some minutes and try again!"
        304, 400, 401, 403, 409, 422 -> "Network error. Please try again. If the problem persists, contact devz"
        else -> "Something went wrong, very sorry about this. Please try again."
    }
    activity.runOnUiThread {
        message.toast(activity)
    }
}

fun setupErrorHandling(intent: Intent, context: Context) {
    if (intent.getStringExtra("error") != null) {
        "Sorry the app crashed, something went wrong".toast(context)
        Log.e("error", intent.getStringExtra("error"))
    }

    Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(context))
}
