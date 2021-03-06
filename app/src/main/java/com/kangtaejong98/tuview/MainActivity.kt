package com.kangtaejong98.tuview

import android.os.Bundle
import com.kangtaejong98.tuview.base.BaseActivity
import com.kangtaejong98.tuview.databinding.ActivityMainBinding
import com.kangtaejong98.tuview.notification.NotificationFactory
import com.kangtaejong98.tuview.view.FullScreenChromeClient

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.tuView.onDestroy()
    }

    override fun onBackPressed() {
        if (binding.tuView.canGoBack()) {
            binding.tuView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun init() {
        initNotificationChannel()
        initActionBar()
        initTuView()
    }

    private fun initNotificationChannel() {
        NotificationFactory.createNotificationChannel(this)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun initTuView() {
        binding.tuView.webChromeClient = FullScreenChromeClient(this)
    }
}