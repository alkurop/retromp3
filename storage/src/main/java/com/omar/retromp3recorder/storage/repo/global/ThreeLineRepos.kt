package com.omar.retromp3recorder.storage.repo.global

import android.content.Context
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.dto.FeatureFlagsCollection
import com.omar.retromp3recorder.dto.LogEvent
import com.omar.retromp3recorder.dto.MediaProjectionState
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.storage.repo.common.PublishSubjectRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecorderPrefsRepo @Inject constructor() :
    BehaviorSubjectRepo<Mp3VoiceRecorder.RecorderPrefs>()

@Singleton
class FeatureFlagRepo @Inject constructor() :
    BehaviorSubjectRepo<FeatureFlagsCollection>()


@Singleton
class MediaProjectionStateRepo @Inject constructor() :
    BehaviorSubjectRepo<MediaProjectionState>(MediaProjectionState())

@Singleton
class LogsRepo @Inject constructor() : PublishSubjectRepo<LogEvent>()

@Singleton
class ToastRepo @Inject constructor(private val context: Context) : PublishSubjectRepo<String>() {
    fun onNext(stringer: Stringer) {
        onNext(stringer.bell(context))
    }
}
