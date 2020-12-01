package com.kangtaejong98.tuview.view

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.FrameLayout


class FullScreenChromeClient(private val activity: Activity) : WebChromeClient() {
    private var view: View? = null
    private var callback: CustomViewCallback? = null

    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        this.view = view
        this.callback = callback

        setFullScreen(true)

        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
        (activity.window.decorView as FrameLayout).addView(
            view, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun onHideCustomView() {
        callback?.onCustomViewHidden()
        view?.let { (activity.window.decorView as FrameLayout).removeView(it) }

        setFullScreen(false)
    }

    private fun setFullScreen(boolean: Boolean) {
        if (boolean) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}