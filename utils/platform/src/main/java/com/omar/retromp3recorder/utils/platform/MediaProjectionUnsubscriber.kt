package com.omar.retromp3recorder.utils.platform

import android.media.projection.MediaProjection
import android.os.Handler
import android.os.Looper
import javax.inject.Inject

class MediaProjectionUnsubscriber @Inject constructor() {
    fun onNewProjection(projection: MediaProjection, onUnsubscribed: () -> Unit) {
        projection.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                onUnsubscribed()
                projection.unregisterCallback(this)
            }
        }, Handler(Looper.getMainLooper()))
    }
}
