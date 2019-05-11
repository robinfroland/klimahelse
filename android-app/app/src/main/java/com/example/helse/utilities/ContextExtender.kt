package com.example.helse.utilities

import android.content.Context
import android.widget.Toast

fun String.toast(context: Context?, duration: Int = Toast.LENGTH_LONG): Toast? {
    if (context === null) {
        println("CONTEXT IS NULL")
        return null
    }
    return Toast.makeText(context, this, duration).apply { show() }
}