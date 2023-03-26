package com.omar.retromp3recorder.utils.domain.repo

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

open class StateFlowRepo<T : Any>(default: T? = null) : Repo<T, T> {
    private val flow = MutableSharedFlow<T>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        if (default != null) {
            flow.tryEmit(default)
        }
    }

    override suspend fun emit(input: T) {
        flow.emit(input)
    }

    override fun flow(): Flow<T> = flow
}
