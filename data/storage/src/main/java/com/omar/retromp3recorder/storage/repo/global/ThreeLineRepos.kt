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
class LogRepo @Inject constructor() : PublishSubjectRepo<LogEvent>(30)

@Singleton
class ToastRepo @Inject constructor(private val context: Context) :  StateFlowRepo<String>() {
    suspend fun emit(stringer: Stringer) {
        emit(stringer.bell(context))
    }
}
