package com.omar.retromp3recorder.storage.repo

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.dto.LogEvent
import com.omar.retromp3recorder.dto.PlayerFeatures
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.storage.repo.common.PublishSubjectRepo
import com.omar.retromp3recorder.utils.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecorderPrefsRepo @Inject constructor() :
    BehaviorSubjectRepo<Mp3VoiceRecorder.RecorderPrefs>()

@Singleton
class FeatureFlagRepo @Inject constructor() :
    BehaviorSubjectRepo<FeatureFlagsCollection>()

@Singleton
class JoinedProgressRepo @Inject constructor() :
    BehaviorSubjectRepo<JoinedProgress>(JoinedProgress.Hidden)

@Singleton
class MediaProjectionStateRepo @Inject constructor() :
    BehaviorSubjectRepo<MediaProjectionState>(MediaProjectionState())

@Singleton
class LogsRepo @Inject constructor() : PublishSubjectRepo<LogEvent>()


//todo should move to local

@Singleton
class CurrentFileRepo @Inject constructor() :
    BehaviorSubjectRepo<Optional<out FileWrapper>>(Optional.empty())

@Singleton
class RangeBarResetBus @Inject constructor() :
    BehaviorSubjectRepo<Shell<Any>>(Shell.empty())
