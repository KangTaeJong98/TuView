package com.kangtaejong98.tuview.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kangtaejong98.tuview.Protocol.PREVIOUS
import com.kangtaejong98.tuview.notification.NotificationData
import com.kangtaejong98.tuview.notification.NotificationFactory
import com.kangtaejong98.tuview.view.TuView

class RunningReceiver() : BroadcastReceiver() {
    companion object {
        var tuView: TuView? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        intent.action?.let {
            when(it) {
                PREVIOUS -> {
                    tuView?.goBack()
                }

                else -> {

                }
            }
        }
        NotificationFactory.createNotification(context, NotificationData())
    }
}