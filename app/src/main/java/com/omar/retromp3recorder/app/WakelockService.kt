package com.omar.retromp3recorder.app

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import com.omar.retromp3recorder.app.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WakelockService : Service() {
    private val notificationManager: NotificationManager by lazy {
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val wakeLock: PowerManager.WakeLock by lazy {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_ID)
    }

    override fun onBind(intent: Intent): Nothing? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        createNotificationChannel()
        obtainWakeLock()
        showRecordingNotification()
    }

    override fun onDestroy() {
        releaseWakeLock()
    }

    private fun showRecordingNotification() {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, FLAG_MUTABLE)
            }
        val notification = NotificationCompat.Builder(this, WAKELOCK_SERVICE_CHANNEL)
            .setSilent(true)
            .setContentTitle(getText(R.string.app_name))
            .setContentText(getText(R.string.wake_lock_recording))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setTicker(getText(R.string.app_name))
            .setVisibility(VISIBILITY_PUBLIC)
            .build()
        startForeground(WAKELOCK_NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(WAKELOCK_SERVICE_CHANNEL, name, importance)
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("WakelockTimeout")
    private fun obtainWakeLock() {
        wakeLock.acquire()
    }

    private fun releaseWakeLock() {
        wakeLock.release()
    }

    @Keep
    companion object {
        private const val WAKELOCK_ID = "RetroMp3Recorder:Wakywaky"
        const val WAKELOCK_SERVICE_CHANNEL = "WAKELOCK_SERVICE_CHANNEL"
        const val WAKELOCK_NOTIFICATION_ID = 111
    }
}
