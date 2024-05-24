package com.danialtavakoli.broadcastworker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.danialtavakoli.broadcastworker.ui.theme.BroadcastWorkerTheme
import com.danialtavakoli.broadcastworker.utils.LogUtils
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BroadcastWorkerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
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
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BroadcastWorkerTheme {
        Greeting("Android")
    }
}