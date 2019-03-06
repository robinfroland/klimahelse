package com.example.helse

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var notificationService: NotificationService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationService = NotificationService(this)

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