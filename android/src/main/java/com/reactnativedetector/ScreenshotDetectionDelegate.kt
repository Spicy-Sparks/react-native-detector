package com.reactnativedetector

import android.Manifest
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import androidx.core.content.ContextCompat
import android.database.Cursor
import android.util.Log
import java.lang.Exception


class ScreenshotDetectionDelegate(val context: Context, val listener: ScreenshotDetectionListener) {
    lateinit var contentObserver: ContentObserver

    var isListening = false

    fun startScreenshotDetection() {
        contentObserver = object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                if(uri != null && uri.toString().endsWith("/media"))
                    return
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