package com.kangtaejong98.tuview.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.kangtaejong98.tuview.Protocol.RUNNING_NOTIFICATION_ID
import com.kangtaejong98.tuview.notification.NotificationData
import com.kangtaejong98.tuview.notification.NotificationFactory

class RunningService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = intent?.getSerializableExtra("data") as? NotificationData ?: NotificationData("", "")

        startForeground(RUNNING_NOTIFICATION_ID, NotificationFactory.createNotification(this, data))
        return START_STICKY
    }
}