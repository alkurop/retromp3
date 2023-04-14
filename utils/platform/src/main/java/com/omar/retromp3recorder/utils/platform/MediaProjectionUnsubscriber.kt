package com.omar.retromp3recorder.utils.platform

import android.media.projection.MediaProjection
import android.os.Handler
import android.os.Looper
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import kotlinx.coroutines.launch

class MediaProjectionUnsubscriber(
    val audioCoroutineContext: AudioCoroutineContext
) {
    fun onNewProjection(projection: MediaProjection, onUnsubscribed: () -> Unit) {
        projection.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                audioCoroutineContext.launch {
                    onUnsubscribed()
                }
                projection.unregisterCallback(this)
            }
        }, Handler(Looper.getMainLooper()))
    }
}
