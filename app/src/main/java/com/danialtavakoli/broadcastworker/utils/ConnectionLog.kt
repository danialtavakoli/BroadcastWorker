package com.danialtavakoli.broadcastworker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ConnectionLog(
    val time: String, val logType: String, val status: String
) {
    companion object {
        fun create(logType: String, status: String): ConnectionLog {
            val currentTime =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            return ConnectionLog(currentTime, logType, status)
        }
    }
}
