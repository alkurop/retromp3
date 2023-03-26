package com.omar.retromp3recorder.utils.platform

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

    open fun tryNext(next: T) {
        flow.tryEmit(next)
    }

    suspend fun emit(next: T) {
        flow.emit(next)
    }

    fun flow(): Flow<T> = flow

    suspend fun first(): T = flow.first()

}
