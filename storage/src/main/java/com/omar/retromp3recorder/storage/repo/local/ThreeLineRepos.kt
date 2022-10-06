package com.omar.retromp3recorder.storage.repo.local

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.MenuExecutable
import com.omar.retromp3recorder.dto.PlayerControls
import com.omar.retromp3recorder.dto.Track
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.Optional
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

@Track
class MenuPopupBus @Inject constructor() :
    BehaviorSubjectRepo<Optional<MenuExecutable>>(Optional.empty())
