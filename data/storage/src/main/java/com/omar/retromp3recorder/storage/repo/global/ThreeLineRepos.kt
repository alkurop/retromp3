package com.omar.retromp3recorder.storage.repo.global

import android.content.Context
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.domain.platform.MediaProjectionState
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.common.StateFlowRepo
import com.omar.retromp3recorder.storage.repo.common.PublishSubjectRepo
import com.omar.retromp3recorder.domain.platform.LogEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecorderPrefsRepo @Inject constructor() :
    StateFlowRepo<Mp3VoiceRecorder.RecorderPrefs>()

@Singleton
class FeatureFlagRepo @Inject constructor() :
    StateFlowRepo<FeatureFlagsCollection>()


@Singleton
class MediaProjectionStateRepo @Inject constructor() :
    StateFlowRepo<MediaProjectionState>(MediaProjectionState())

@Singleton
class LogsRepo @Inject constructor() : PublishSubjectRepo<LogEvent>()

@Singleton
class ToastRepo @Inject constructor(private val context: Context) : PublishSubjectRepo<String>() {
    fun onNext(stringer: Stringer) {
        onNext(stringer.bell(context))
    }
}
