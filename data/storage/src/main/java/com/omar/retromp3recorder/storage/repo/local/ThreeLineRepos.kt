package com.omar.retromp3recorder.storage.repo.local

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FileWrapper
import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.utils.domain.repo.StateFlowRepo
import com.omar.retromp3recorder.utils.domain.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentFileRepo @Inject constructor() :
    StateFlowRepo<Optional<out FileWrapper>>(Optional.empty())

@Singleton
class RangeBarResetBus @Inject constructor() :
    StateFlowRepo<Shell<Any>>(Shell.empty())

@Singleton
class PlayerControlsRepo @Inject constructor() :
    StateFlowRepo<PlayerControls>(PlayerControls())
