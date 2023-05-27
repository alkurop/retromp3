package com.omar.retromp3recorder.app.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.io.downloader.LanguageDownloadNotificationSender
import com.omar.retromp3recorder.io.downloader.LanguageDownloadStatus
import com.omar.retromp3recorder.io.language.getFilename
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidLanguageDownloadNotificationSender @Inject constructor(
    @ApplicationContext private val context: Context
) : LanguageDownloadNotificationSender {

    override fun sendNotification(status: LanguageDownloadStatus) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notificationId = status.language.getFilename().hashCode()

        if (status is LanguageDownloadStatus.FinishedSuccess) {
            NotificationManagerCompat.from(context).cancel(notificationId)
        } else {
            val name = "VERBOSE_NOTIFICATION_CHANNEL_NAME"
            val description = "VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(FILE_DOWNLOAD_CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)

            // Create the notification
            val builder = NotificationCompat.Builder(context, FILE_DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("NOTIFICATION_TITLE")
                .setContentText("message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))

            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
            // Show the notification
        }
    }
}

private const val FILE_DOWNLOAD_CHANNEL_ID = "FILE_DOWNLOAD_CHANNEL_ID"
