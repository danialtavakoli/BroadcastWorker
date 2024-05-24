package com.danialtavakoli.broadcastworker

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.danialtavakoli.broadcastworker.utils.ConnectionLog
import com.danialtavakoli.broadcastworker.utils.LogUtils

class StatusCheckWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val isBluetoothEnabled = isBluetoothEnabled()
        val isAirplaneModeOn = isAirplaneModeOn()

        val bluetoothStatus = if (isBluetoothEnabled) "Connected" else "Disconnected"
        val airplaneStatus = if (isAirplaneModeOn) "Connected" else "Disconnected"

        Log.i("worker_airplane", "Bluetooth: $bluetoothStatus, Airplane Mode: $airplaneStatus")

        // Write logs to file
        val bluetoothLog = ConnectionLog.create("Bluetooth", bluetoothStatus)
        val airplaneLog = ConnectionLog.create("Airplane", airplaneStatus)
        LogUtils.writeLog(applicationContext, bluetoothLog)
        LogUtils.writeLog(applicationContext, airplaneLog)

        return Result.success()
    }

    private fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.isEnabled == true
    }

    private fun isAirplaneModeOn(): Boolean {
        return Settings.Global.getInt(
            applicationContext.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
    }
}
