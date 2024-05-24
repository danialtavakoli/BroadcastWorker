package com.danialtavakoli.broadcastworker

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danialtavakoli.broadcastworker.ui.theme.BroadcastWorkerTheme
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
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        text = internetConnectionText
                    )
                }
            }
        }
        setupPeriodicWorker()


        // Read and print logs for testing
        val logs = LogUtils.readLogs(this)
        logs.forEach {
            Log.d("ConnectionLog", it.toString())
        }
    }

    private fun setupPeriodicWorker() {
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<StatusCheckWorker>(15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "StatusCheckWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )

        // Read and print logs for testing
        val logs = LogUtils.readLogs(this)
        logs.forEach {
            Log.d("ConnectionLog", it.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetReceiver)
        stopService(Intent(this, InternetService::class.java))
    }
}

@Composable
fun MainScreen(text: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (text == "Internet Connected") Color.Cyan else Color.Red
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BroadcastWorkerTheme {
        MainScreen("Internet Connected")
    }
}
