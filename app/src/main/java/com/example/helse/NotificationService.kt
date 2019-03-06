package com.example.helse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

class NotificationService(private val context: Context) {
    private lateinit var builder: NotificationCompat.Builder

    init {
        setupNotification()
    }

    private fun setupNotification() {
        // show notification
        builder = NotificationCompat.Builder(/*Find a suitable context..*/context, "123")
            .setSmallIcon(R.drawable.notification_icon).setContentTitle("A notification")
            .setContentText("Tailored to you").setPriority(NotificationCompat.PRIORITY_DEFAULT)

        createNotificationChannel()
    }

    fun sendNotification() {
        with(NotificationManagerCompat.from(context)) {
            notify(123, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "A test"
            val descriptionText = "Made to serve as an example notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("123", channelName, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}