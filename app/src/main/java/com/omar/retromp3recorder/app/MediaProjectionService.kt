package com.omar.retromp3recorder.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import com.omar.retromp3recorder.app.WakelockService.Companion.WAKELOCK_SERVICE_CHANNEL
import com.omar.retromp3recorder.app.main.MainActivity
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MediaProjectionService : Service() {
    @Inject
    lateinit var mediaProjectionRepo: MediaProjectionStateRepo

    @Inject
    lateinit var audioCoroutineContext: AudioCoroutineContext

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent): Nothing? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        showRecordingNotification()
        observeStopBus()
    }

    override fun onDestroy() {
        audioCoroutineContext.cancel()
    }

    private fun observeStopBus() {
        audioCoroutineContext.launch {
            mediaProjectionRepo.flow().collect { item ->
                val shouldStop = item.stop.ghost != null
                if(shouldStop) {
                    withContext(Dispatchers.Main) {
                        stopSelf()
                        hideNotification()
                    }
                }
            }
        }
    }


    private fun showRecordingNotification() {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_MUTABLE
                    )
                } else {
                    //todo test it with flag immutable
                    PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_MUTABLE
                    )
                }
            }
        val notification = NotificationCompat.Builder(this, WAKELOCK_SERVICE_CHANNEL)
            .setContentTitle(getText(R.string.projection_notification_title))
            .setSilent(true)
            .setContentText(getText(R.string.projection_notification_message))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setTicker(getText(R.string.app_name))
            .setVisibility(VISIBILITY_PUBLIC)
            .build()
        startForeground(MEDIA_PROJECTION_NOTIFICATION_ID, notification)
    }

    private fun hideNotification() {
        notificationManager.cancel(MEDIA_PROJECTION_NOTIFICATION_ID)
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(WAKELOCK_SERVICE_CHANNEL, name, importance).apply {
            description = descriptionText
        }

        notificationManager.createNotificationChannel(channel)
    }
}

private const val MEDIA_PROJECTION_NOTIFICATION_ID = 222
