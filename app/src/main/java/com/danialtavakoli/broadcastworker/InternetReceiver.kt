package com.danialtavakoli.broadcastworker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class InternetReceiver(private val updateInternetConnectionText: (String) -> Unit) :
    BroadcastReceiver() {
    private val notificationManager = NotificationManager()
    private val permissionManager = PermissionManager()
    private val connectionManager = ConnectionManager()

    override fun onReceive(context: Context, intent: Intent?) {
        if (permissionManager.checkNotificationPermission(context)) {
            if (connectionManager.isInternetConnected(context)) {
                updateInternetConnectionText("Internet Connected")
                notificationManager.showNotification(context, "Internet Connected")
            } else {
                updateInternetConnectionText("Internet Disconnected")
                notificationManager.showNotification(context, "Internet Disconnected")
            }
        }
    }

}
