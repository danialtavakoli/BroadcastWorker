package com.danialtavakoli.broadcastworker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danialtavakoli.broadcastworker.utils.ConnectionLog

@Composable
fun MainScreen(text: String, logs: List<ConnectionLog>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (text == "Internet Connected") Color.Cyan else Color.Red
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(logs) { log ->
                LogCard(log = log)
            }
        }
    }
}

@Composable
fun LogCard(log: ConnectionLog) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0F7FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = log.time,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = log.logType,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = log.status,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = if (log.status == "Connected" || log.status == "Enabled") Color.Green else Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val sampleLogs = listOf(
        ConnectionLog("2024-05-24 12:00:00", "Internet", "Connected"),
        ConnectionLog("2024-05-24 11:45:00", "Bluetooth", "Disconnected"),
        ConnectionLog("2024-05-24 11:30:00", "Airplane", "Enabled")
    )
    MainScreen("Internet Connected", sampleLogs)
}