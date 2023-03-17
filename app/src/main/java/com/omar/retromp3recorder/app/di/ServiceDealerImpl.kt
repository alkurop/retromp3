package com.omar.retromp3recorder.app.di

import android.content.Context
import android.content.Intent
import com.omar.retromp3recorder.app.MediaProjectionService
import com.omar.retromp3recorder.app.WakelockService
import com.omar.retromp3recorder.utils.domain.ServiceDealer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ServiceDealerImpl @Inject constructor(
   @ApplicationContext private val context: Context
) : ServiceDealer {
    override fun startWakelockService() {
        with(context) {
            val startIntent = Intent(this, WakelockService::class.java)
            context.startForegroundService(startIntent)
        }
    }

    override fun stopWakelockService() {
        context.stopService(Intent(context, WakelockService::class.java))
    }

    override fun startMediaProjectionService() {
        with(context) {
            val startIntent = Intent(this, MediaProjectionService::class.java)
            context.startForegroundService(startIntent)
        }
    }

    override fun stopMediaProjectionService() {
        context.stopService(Intent(context, MediaProjectionService::class.java))
    }
}
