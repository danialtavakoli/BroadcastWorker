package com.danialtavakoli.broadcastworker

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class InternetReceiver(private val updateInternetConnectionText: (String) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (isInternetConnected(context)) {
            if (checkNotificationPermission(context)) {
                updateInternetConnectionText("Internet Connected")
                showNotification(context, "Internet Connected")
            }
        } else {
            if (checkNotificationPermission(context)) {
                updateInternetConnectionText("Internet Disconnected")
                showNotification(context, "Internet Disconnected")
            }
        }
    }

    private fun isInternetConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun showNotification(context: Context, message: String) {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            createNotificationChannel(context)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Internet Status")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        } else {
            Toast.makeText(
                context, "Permission to show notifications not granted", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkNotificationPermission(context: Context): Boolean {
        return if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
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
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "InternetStatusChannel"
            val descriptionText = "Channel for showing internet status"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "InternetStatusChannel"
        private const val NOTIFICATION_ID = 101
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 102
    }
}
