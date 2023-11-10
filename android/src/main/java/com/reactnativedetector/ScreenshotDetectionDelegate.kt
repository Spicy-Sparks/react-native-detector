package com.reactnativedetector

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore

class ScreenshotDetectionDelegate(val context: Context, val listener: ScreenshotDetectionListener) {
    lateinit var contentObserver: ContentObserver

    var isListening = false
    var lastUri = ""

    fun startScreenshotDetection() {
        contentObserver = object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                if(uri != null && (uri.toString().endsWith("/media") || uri.toString() == lastUri))
                    return
                lastUri = uri.toString()
                onScreenCaptured()
            }
        }

        context.contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                contentObserver)
        isListening = true
    }

    fun stopScreenshotDetection() {
        context.getContentResolver().unregisterContentObserver(contentObserver)
        isListening = false
    }

    private fun onScreenCaptured() {
        listener.onScreenCaptured()
    }
}

interface ScreenshotDetectionListener {
    fun onScreenCaptured()
    fun onScreenCapturedWithDeniedPermission()
}