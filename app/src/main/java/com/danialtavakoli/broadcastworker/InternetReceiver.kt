package com.danialtavakoli.broadcastworker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.danialtavakoli.broadcastworker.utils.ConnectionLog
import com.danialtavakoli.broadcastworker.utils.ConnectionManager
import com.danialtavakoli.broadcastworker.utils.LogUtils
import com.danialtavakoli.broadcastworker.utils.NotificationManager
import com.danialtavakoli.broadcastworker.utils.PermissionManager

class InternetReceiver(private val updateInternetConnectionText: (String) -> Unit) :
    BroadcastReceiver() {
    private val notificationManager = NotificationManager()
    private val permissionManager = PermissionManager()
    private val connectionManager = ConnectionManager()

    override fun onReceive(context: Context, intent: Intent?) {
        val logType = "Internet"
        if (permissionManager.checkNotificationPermission(context)) {
            val status = if (connectionManager.isInternetConnected(context)) {
                updateInternetConnectionText("Internet Connected")
                notificationManager.showNotification(context, "Internet Connected")
                "Connected"
            } else {
                updateInternetConnectionText("Internet Disconnected")
                notificationManager.showNotification(context, "Internet Disconnected")
                "Disconnected"
            }
            val log = ConnectionLog.create(logType, status)
            LogUtils.writeLog(context, log)
        }
    }
}
