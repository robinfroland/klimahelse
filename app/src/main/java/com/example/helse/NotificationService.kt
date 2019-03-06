package com.example.helse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

class NotificationService(private val context: Context) {
    private lateinit var onResumeNotification: NotificationCompat.Builder
    private lateinit var onPauseNotification: NotificationCompat.Builder

    enum class NotificationTypes {
        Resume, Pause
    }

    init {
        setupNotification()
    }

    private fun setupNotification() {
        // Create notifications
        onPauseNotification = NotificationCompat.Builder(context, "123")
            .setSmallIcon(R.drawable.notification_icon).setContentTitle("You paused me")
            .setContentText("It's okay").setPriority(NotificationCompat.PRIORITY_DEFAULT)

        onResumeNotification = NotificationCompat.Builder(context, "456")
            .setSmallIcon(R.drawable.notification_icon).setContentTitle("Brought me back to life!!")
            .setContentText("Many thanks").setPriority(NotificationCompat.PRIORITY_DEFAULT)

        createNotificationChannel()
    }

    fun sendNotification(type: NotificationTypes) {
        // Show notification
        when (type) {
            NotificationTypes.Resume -> with(NotificationManagerCompat.from(context)) {
                notify(456, onResumeNotification.build())
            }
            NotificationTypes.Pause -> with(NotificationManagerCompat.from(context)) {
                notify(123, onPauseNotification.build())
            }
        }
    }

    private fun createNotificationChannel() {
        // Creating a notificationChannel is required in API level 26 and higher
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