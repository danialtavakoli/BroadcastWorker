package com.danialtavakoli.broadcastworker

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.danialtavakoli.broadcastworker.ui.theme.BroadcastWorkerTheme
import com.danialtavakoli.broadcastworker.utils.ConnectionLog
import com.danialtavakoli.broadcastworker.utils.LogUtils
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private var internetConnectionText by mutableStateOf("")

    private val internetReceiver = InternetReceiver { newText ->
        updateInternetConnectionText(newText)
    }

    private fun updateInternetConnectionText(newText: String) {
        internetConnectionText = newText
    }

    private fun registerReceiver() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(internetReceiver, filter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver()
        startService(Intent(this, InternetService::class.java))
        enableEdgeToEdge()
        setContent {
            BroadcastWorkerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var logs by remember { mutableStateOf(emptyList<ConnectionLog>()) }
                    logs = LogUtils.readLogs(this).sortedByDescending { it.time }
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        text = internetConnectionText,
                        logs = logs
                    )
                }
            }
        }
        setupPeriodicWorker()
    }

    private fun setupPeriodicWorker() {
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<StatusCheckWorker>(15, TimeUnit.MINUTES).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "StatusCheckWorker", ExistingPeriodicWorkPolicy.UPDATE, periodicWorkRequest
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetReceiver)
        stopService(Intent(this, InternetService::class.java))
    }
}
