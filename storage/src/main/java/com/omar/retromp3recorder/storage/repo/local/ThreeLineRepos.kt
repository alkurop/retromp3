package com.omar.retromp3recorder.storage.repo.local

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.dto.PlayerFeatures
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.Optional
import javax.inject.Inject
import javax.inject.Singleton

//todo should move to local scope
@Singleton
class CurrentFileRepo @Inject constructor() :
    BehaviorSubjectRepo<Optional<out FileWrapper>>(Optional.empty())

@Singleton
class RangeBarResetBus @Inject constructor() :
    BehaviorSubjectRepo<Shell<Any>>(Shell.empty())

@Singleton
class JoinedProgressRepo @Inject constructor() :
    BehaviorSubjectRepo<JoinedProgress>(JoinedProgress.Hidden)

@Singleton
class PlayerControlsRepo @Inject constructor() :
    BehaviorSubjectRepo<PlayerFeatures>(PlayerFeatures())
