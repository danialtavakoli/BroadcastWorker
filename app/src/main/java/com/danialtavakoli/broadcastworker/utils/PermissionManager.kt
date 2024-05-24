package com.danialtavakoli.broadcastworker.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager {

    private val notificationPermissionRequestCode = 102

    fun checkNotificationPermission(context: Context): Boolean {
        return if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission(context)
            false
        } else {
            true
        }
    }

    private fun requestNotificationPermission(context: Context) {
        if (context is Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    notificationPermissionRequestCode
                )
            }
        }
    }
}