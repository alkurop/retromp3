package com.omar.retromp3recorder.utils.domain.repo

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

open class StateFlowRepo<T : Any>(default: T? = null) {
    private val flow = MutableSharedFlow<T>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        if (default != null) {
            flow.tryEmit(default)
        }
    }

    open suspend fun emit(input: T) {
        flow.emit(input)
    }


    open fun flow(): Flow<T> = flow
}

suspend fun <Out : Any> StateFlowRepo<Out>.first() = flow().first()
