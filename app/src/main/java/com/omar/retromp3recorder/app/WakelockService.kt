package com.omar.retromp3recorder.app

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import com.omar.retromp3recorder.app.ui.main.MainActivity
import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class WakelockService : Service() {
    @Inject
    lateinit var audioStateMapper: AudioStateMapper
    private val compositeDisposable = CompositeDisposable()
    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
        App.appComponent.inject(this)
        createNotificationChannel()
        observeAudioState()
        obtainWakeLock()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        releaseWakeLock()
    }

    private fun observeAudioState() {
        Completable
            .merge(
                listOf(
                    audioStateMapper.observe().ofType(AudioState.Idle::class.java)
                        .flatMapCompletable { Completable.fromAction { stopSelf(); hideNotification() } },
                    audioStateMapper.observe().ofType(AudioState.Recording::class.java)
                        .flatMapCompletable { Completable.fromAction { showRecordingNotification() } },
                )
            )
            .subscribe()
            .disposedBy(compositeDisposable)
    }


    private fun showRecordingNotification() {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, FLAG_MUTABLE)
            }
        val notification = NotificationCompat.Builder(this, WAKELOCK_SERVICE_CHANNEL)
            .setSilent(true)
            .setContentTitle(getText(R.string.app_name))
            .setContentText(getText(R.string.recording))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setTicker(getText(R.string.app_name))
            .setVisibility(VISIBILITY_PUBLIC)
            .build()
        startForeground(WAKELOCK_NOTIFICATION_ID, notification)
    }

    private fun hideNotification() {
        notificationManager.cancel(WAKELOCK_NOTIFICATION_ID)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(WAKELOCK_SERVICE_CHANNEL, name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
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
