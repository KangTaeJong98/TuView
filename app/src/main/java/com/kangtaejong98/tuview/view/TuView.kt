package com.kangtaejong98.tuview.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kangtaejong98.tuview.Protocol.RUNNING_NOTIFICATION_ID
import com.kangtaejong98.tuview.Protocol.YOUTUBE
import com.kangtaejong98.tuview.notification.NotificationData
import com.kangtaejong98.tuview.notification.NotificationFactory
import com.kangtaejong98.tuview.receiver.RunningReceiver
import com.kangtaejong98.tuview.service.RunningService
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.properties.Delegates

class TuView : WebView {
    private val routine: Job

    private var isRunning = false

    private var updatedUrl by Delegates.observable(YOUTUBE) { _, oldValue, newValue ->
        if (oldValue == newValue) {
            return@observable
        }

        if (newValue.contains("watch?v=")) {
            val data = NotificationData.build(getDocument(newValue))

            if (!isRunning) {
                isRunning = true
                context.startForegroundService(Intent(context, RunningService::class.java).putExtra("data", data))
            } else {
                val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

                manager.notify(RUNNING_NOTIFICATION_ID, NotificationFactory.createNotification(context, data))
            }
        } else {
            isRunning = false
            context.stopService(Intent(context, RunningService::class.java))
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        settings.javaScriptEnabled = true
        webViewClient = WebViewClient()
        loadUrl(YOUTUBE)

        routine = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                delay(1000L)
                updatedUrl = url ?: ""
            }
        }

        RunningReceiver.tuView = this
    }

    fun onDestroy() {
        routine.cancel()
        context.stopService(Intent(context, RunningService::class.java))
    }

    private fun getDocument(url: String): Document {
        return runBlocking(Dispatchers.IO) {
            Jsoup.connect(url).get()
        }
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility == View.VISIBLE) {
            super.onWindowVisibilityChanged(visibility)
        }
    }


}