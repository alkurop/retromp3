package com.omar.retromp3recorder.storage.repo

import android.media.projection.MediaProjection
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioSourceRepo @Inject constructor() :
    BehaviorSubjectRepo<Mp3VoiceRecorder.AudioSourcePref>()

@Singleton
class BitRateRepo @Inject constructor() :
    BehaviorSubjectRepo<Mp3VoiceRecorder.BitRate>()

@Singleton
class CurrentFileRepo @Inject constructor() :
    BehaviorSubjectRepo<Optional<FileWrapper>>(Optional.empty()) {
    override fun onNext(next: Optional<FileWrapper>) {
        super.onNext(next)
    }
}

@Singleton
class FeatureFlagRepo @Inject constructor() :
    BehaviorSubjectRepo<FeatureFlagsCollection>()

@Singleton
class JoinedProgressRepo @Inject constructor() :
    BehaviorSubjectRepo<JoinedProgress>(JoinedProgress.Hidden)

@Singleton
class MediaProjectionRepo @Inject constructor() :
    BehaviorSubjectRepo<Optional<MediaProjection>>(Optional.empty())

@Singleton
class MediaProjectionRequestBus @Inject constructor() :
    BehaviorSubjectRepo<Shell<Any>>(Shell.empty())

@Singleton
class MediaProjectionStopBus @Inject constructor() :
    BehaviorSubjectRepo<Shell<Any>>(Shell.empty())

@Singleton
class SampleRateRepo @Inject constructor() :
    BehaviorSubjectRepo<Mp3VoiceRecorder.SampleRate>()

@Singleton
class SeekRepo @Inject constructor() :
    BehaviorSubjectRepo<Optional<Int>>(Optional.empty())

@Singleton
class LoadingStateRepo @Inject constructor() : BehaviorSubjectRepo<Loading>(Loading.Not)

sealed class Loading {
    object Not : Loading()
    data class Is(val percent: Int) : Loading()
}