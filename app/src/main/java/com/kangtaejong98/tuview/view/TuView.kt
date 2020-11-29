package com.kangtaejong98.tuview.view

import android.app.*
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.NotificationCompat
import com.kangtaejong98.tuview.MainActivity
import com.kangtaejong98.tuview.Protocol.NOTIFICATION_CHANNEL_ID
import com.kangtaejong98.tuview.Protocol.NOTIFICATION_CHANNEL_NAME
import com.kangtaejong98.tuview.Protocol.NOTIFICATION_IMPORTANCE
import com.kangtaejong98.tuview.Protocol.RUNNING_NOTIFICATION_ID
import com.kangtaejong98.tuview.Protocol.YOUTUBE
import com.kangtaejong98.tuview.R
import com.kangtaejong98.tuview.service.RunningService
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.properties.Delegates

class TuView : WebView {
    private val routine: Job

    private var isRunning by Delegates.observable(false) { _, oldValue, newValue ->
        if (newValue) {
            if (!oldValue) {
                context.startForegroundService(Intent(context, RunningService::class.java))
            }
        } else {
            context.stopService(Intent(context, RunningService::class.java))
        }
    }

    private var updatedUrl by Delegates.observable(YOUTUBE) { _, oldValue, newValue ->
        if (oldValue == newValue) {
            return@observable
        }

        if (newValue.contains("watch?v=")) {
            isRunning = true
            val notification = createNotification(getDocument(newValue))
            val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

            createNotificationChannel()
            manager.notify(RUNNING_NOTIFICATION_ID, notification)
        } else {
            isRunning = false
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

    private fun getTitle(document: Document): String {
        return try {
            var title = ""
            val elements = document.getElementsByTag("meta")
            for (element in elements) {
                if (element.attr("name") == "title") {
                    title = element.attr("content")
                    break
                }
            }

            title
        } catch (e: Exception) {
            "Title"
        }
    }

    private fun getAuthor(document: Document): String {
        return try {
            var author = ""
            val elements = document.getElementsByTag("link")
            for (element in elements) {
                if (element.attr("itemprop") == "name") {
                    author = element.attr("content")
                }
            }

            author
        } catch (e: Exception) {
            "Author"
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NOTIFICATION_IMPORTANCE)
        val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }

    private fun createNotification(title: String, author: String): Notification {
        val contentIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.icon)
            setContentTitle(title)
            setContentText(author)
            setContentIntent(contentIntent)
        }.build()
    }

    private fun createNotification(document: Document): Notification {
        val title = getTitle(document)
        val author = getAuthor(document)

        return createNotification(title, author)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility == View.VISIBLE) {
            super.onWindowVisibilityChanged(visibility)
        }
    }
}