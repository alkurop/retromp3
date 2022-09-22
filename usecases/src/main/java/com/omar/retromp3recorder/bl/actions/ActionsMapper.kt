package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.dto.AudioExecutable
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ActionsMapper @Inject constructor(
    private val cropUC: CropUC
) {
    fun execute(action: AudioExecutable): Completable =
        when (action) {
            AudioExecutable.Crop -> cropUC.execute()
        }
}
