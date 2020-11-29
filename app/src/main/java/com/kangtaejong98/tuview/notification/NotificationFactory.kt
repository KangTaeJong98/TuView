package com.kangtaejong98.tuview.notification

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.kangtaejong98.tuview.MainActivity
import com.kangtaejong98.tuview.Protocol
import com.kangtaejong98.tuview.Protocol.NOTIFICATION_CHANNEL_ID
import com.kangtaejong98.tuview.R

object NotificationFactory {
    fun createNotification(context: Context, data: NotificationData): Notification {
        val contentIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.icon)
            setContentTitle(data.title)
            setContentText(data.author)
            setContentIntent(contentIntent)
        }.build()
    }

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(Protocol.NOTIFICATION_CHANNEL_ID, Protocol.NOTIFICATION_CHANNEL_NAME, Protocol.NOTIFICATION_IMPORTANCE)
        val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }
}