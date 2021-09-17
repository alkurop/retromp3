package com.omar.retromp3recorder.app

import android.content.Context
import android.content.Intent
import android.os.Build
import com.omar.retromp3recorder.utils.ServiceDealer
import javax.inject.Inject

class ServiceDealerImpl @Inject constructor(
    private val context: Context
) : ServiceDealer {
    override fun startWakelockService() {
        with(context) {
            val startIntent = Intent(this, WakelockService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
    }

    override fun startMediaProjectionService() {
        with(context) {
            val startIntent = Intent(this, MediaProjectionService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
    }
}