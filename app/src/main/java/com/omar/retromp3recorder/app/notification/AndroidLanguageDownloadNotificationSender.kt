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
import com.omar.retromp3recorder.domain.LanguageAvailability
import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.io.speech.downloader.LanguageDownloadNotificationSender
import com.omar.retromp3recorder.io.speech.downloader.LanguageDownloadStatus
import com.omar.retromp3recorder.io.language.getFilename
import com.omar.retromp3recorder.storage.repo.global.LanguageAvailabilityRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidLanguageDownloadNotificationSender @Inject constructor(
    @ApplicationContext private val context: Context,
    private val languageAvailabilityRepo: LanguageAvailabilityRepo
) : LanguageDownloadNotificationSender {

    override suspend fun sendNotification(status: LanguageDownloadStatus) {

        languageAvailabilityRepo.updateItem(status.toAvailabilityStatus())

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notificationId = status.language.getFilename().hashCode()
        when (status) {
            is LanguageDownloadStatus.FinishedSuccess -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
            is LanguageDownloadStatus.FinishedWithError -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
            }
            is LanguageDownloadStatus.Progress -> {
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
                    .setContentText("${status.percent} %")
                    .setSilent(true)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)

                NotificationManagerCompat.from(context).notify(notificationId, builder.build())
            }
        }
    }
}

private const val FILE_DOWNLOAD_CHANNEL_ID = "FILE_DOWNLOAD_CHANNEL_ID"


private fun LanguageDownloadStatus.toAvailabilityStatus(): LanguageAvailability = when (this) {
    is LanguageDownloadStatus.FinishedSuccess -> LanguageState.Available
    is LanguageDownloadStatus.FinishedWithError -> LanguageState.ToDownload
    is LanguageDownloadStatus.Progress -> LanguageState.Loading(this.percent)
}.let { LanguageAvailability(this.language, it) }
