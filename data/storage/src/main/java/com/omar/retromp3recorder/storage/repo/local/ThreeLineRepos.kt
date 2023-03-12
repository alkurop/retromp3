package com.omar.retromp3recorder.storage.repo.local

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FileWrapper
import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.storage.repo.common.StateFlowRepo
import com.omar.retromp3recorder.utils.domain.Track
import com.omar.retromp3recorder.utils.platform.Optional
import javax.inject.Inject

@Track
class CurrentFileRepo @Inject constructor() :
    StateFlowRepo<Optional<out FileWrapper>>(Optional.empty())

@Track
class RangeBarResetBus @Inject constructor() :
    StateFlowRepo<Shell<Any>>(Shell.empty())

@Track
class PlayerControlsRepo @Inject constructor() :
    StateFlowRepo<PlayerControls>(PlayerControls())
