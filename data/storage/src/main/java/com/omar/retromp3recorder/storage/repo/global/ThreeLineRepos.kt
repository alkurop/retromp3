package com.omar.retromp3recorder.storage.repo.global

import android.content.Context
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.domain.BillingRequest
import com.omar.retromp3recorder.domain.BillingResponse
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.domain.platform.LogEvent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.domain.repo.PublishSubjectRepo
import com.omar.retromp3recorder.utils.domain.repo.StateFlowRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecorderPrefsRepo @Inject constructor() :
    StateFlowRepo<Mp3VoiceRecorder.RecorderPrefs>(Mp3VoiceRecorder.RecorderPrefs())

@Singleton
class FeatureFlagRepo @Inject constructor() :
    StateFlowRepo<FeatureFlagsCollection>()

@Singleton
class LogRepo @Inject constructor() : PublishSubjectRepo<LogEvent>(30)

@Singleton
class ToastRepo @Inject constructor(@ApplicationContext val context: Context) :
    StateFlowRepo<String>() {
    suspend fun emit(stringer: Stringer) {
        emit(stringer.bell(context))
    }
}

@Singleton
class BillingRequestEventBus @Inject constructor() : PublishSubjectRepo<BillingRequest>(0)

@Singleton
class BillingResultEventBus @Inject constructor() : PublishSubjectRepo<Result<BillingResponse>>(0)
