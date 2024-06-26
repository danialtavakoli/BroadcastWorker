package com.danialtavakoli.broadcastworker.utils

import android.content.Context
import android.net.ConnectivityManager

class ConnectionManager {
    fun isInternetConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
