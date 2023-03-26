package com.omar.retromp3recorder.utils.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

open class PublishSubjectRepo<T : Any>(replay: Int = 0) {
    private val state = MutableSharedFlow<T>(
        replay = replay
    )

    open fun emit(next: T) {
        state.tryEmit(next)
    }


    fun flow(): Flow<T> = state

    suspend fun first(): T = state.first()

}
