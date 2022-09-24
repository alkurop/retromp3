package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.dto.MenuExecutable
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AudioActionsExecutor @Inject constructor(
    private val cropUC: CropUC
) {
    fun execute(action: MenuExecutable): Completable =
        when (action) {
            MenuExecutable.Crop -> cropUC.execute()
        }
}
