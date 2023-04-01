package com.omar.retromp3recorder.app.screens.home

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract

class MediaProjectionActivityResultContract(private val context: Context) :
    ActivityResultContract<Unit, MediaProjection?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return context.mediaProjectionManager.createScreenCaptureIntent()
    }

    override fun parseResult(resultCode: Int, intent: Intent?): MediaProjection? {
        return if (resultCode == ComponentActivity.RESULT_OK && intent != null) {
            context.mediaProjectionManager.getMediaProjection(resultCode, intent)
        } else null
    }
}


val Context.mediaProjectionManager: MediaProjectionManager
    get() = this.getSystemService(ComponentActivity.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
