package com.example.helse.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.helse.MainActivity
import java.io.PrintWriter
import java.io.StringWriter

class ExceptionHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val LINE_SEPARATOR = "\n"

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        val stackTrace = StringWriter()
        if (e == null) {
            throw Error("Omg lol")
        }
        e.printStackTrace(PrintWriter(stackTrace))
        val errorReport = StringBuilder()
        errorReport.append("************ CAUSE OF ERROR ************\n\n")
        errorReport.append(stackTrace.toString())

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ")
        errorReport.append(Build.BRAND)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Device: ")
        errorReport.append(Build.DEVICE)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Model: ")
        errorReport.append(Build.MODEL)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Id: ")
        errorReport.append(Build.ID)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Product: ")
        errorReport.append(Build.PRODUCT)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("\n************ FIRMWARE ************\n")
        errorReport.append("SDK: ")
        errorReport.append(Build.VERSION.SDK_INT)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Release: ")
        errorReport.append(Build.VERSION.RELEASE)
        errorReport.append(LINE_SEPARATOR)
        errorReport.append("Incremental: ")
        errorReport.append(Build.VERSION.INCREMENTAL)
        errorReport.append(LINE_SEPARATOR)

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
