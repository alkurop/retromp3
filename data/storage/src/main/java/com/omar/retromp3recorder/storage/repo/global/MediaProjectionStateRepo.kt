package com.omar.retromp3recorder.storage.repo.global

import com.omar.retromp3recorder.domain.platform.MediaProjectionState
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.domain.repo.StateFlowRepo
import com.omar.retromp3recorder.utils.platform.MediaProjectionUnsubscriber
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaProjectionStateRepo @Inject constructor(
    private val unsubscriber: MediaProjectionUnsubscriber,
    private val scopeJobWrapper: ScopeJobWrapper,
) : StateFlowRepo<MediaProjectionState>(MediaProjectionState()) {
    override suspend fun emit(input: MediaProjectionState) {
        input.mediaProjection.value?.let {
            unsubscriber.onNewProjection(it) {
                scopeJobWrapper.launch {
                    super.emit(input)
                }
            }
        }
        super.emit(input)
    }
}
