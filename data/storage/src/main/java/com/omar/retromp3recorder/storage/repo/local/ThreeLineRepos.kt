package com.omar.retromp3recorder.storage.repo.local

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FileWrapper
import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.utils.generic.Optional
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.domain.Track
import javax.inject.Inject

@Track
class CurrentFileRepo @Inject constructor() :
    BehaviorSubjectRepo<Optional<out FileWrapper>>(Optional.empty())

@Track
class RangeBarResetBus @Inject constructor() :
    BehaviorSubjectRepo<Shell<Any>>(Shell.empty())

@Track
class PlayerControlsRepo @Inject constructor() :
    BehaviorSubjectRepo<PlayerControls>(PlayerControls())
