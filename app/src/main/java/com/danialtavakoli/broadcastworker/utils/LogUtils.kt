package com.danialtavakoli.broadcastworker.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object LogUtils {
    private const val LOG_FILE_NAME = "connection_logs.json"
    private val gson = Gson()
    private val logType = object : TypeToken<MutableList<ConnectionLog>>() {}.type

    fun writeLog(context: Context, log: ConnectionLog) {
        val logFile = File(context.filesDir, LOG_FILE_NAME)
        val logs: MutableList<ConnectionLog> = if (logFile.exists()) {
            FileReader(logFile).use { reader ->
                gson.fromJson(reader, logType)
            }
        } else {
            mutableListOf()
        }

        logs.add(log)

        FileWriter(logFile).use { writer ->
            gson.toJson(logs, writer)
        }
    }

    fun readLogs(context: Context): List<ConnectionLog> {
        val logFile = File(context.filesDir, LOG_FILE_NAME)
        return if (logFile.exists()) {
            FileReader(logFile).use { reader ->
                gson.fromJson(reader, logType)
            }
        } else {
            emptyList()
        }
    }
}
