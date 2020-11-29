package com.kangtaejong98.tuview.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.kangtaejong98.tuview.Protocol.NOTIFICATION_CHANNEL_ID
import com.kangtaejong98.tuview.Protocol.NOTIFICATION_CHANNEL_NAME
import com.kangtaejong98.tuview.Protocol.NOTIFICATION_IMPORTANCE
import com.kangtaejong98.tuview.Protocol.RUNNING_NOTIFICATION_ID

class RunningService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(RUNNING_NOTIFICATION_ID, Notification.Builder(this, NOTIFICATION_CHANNEL_ID).build())

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NOTIFICATION_IMPORTANCE)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }
}