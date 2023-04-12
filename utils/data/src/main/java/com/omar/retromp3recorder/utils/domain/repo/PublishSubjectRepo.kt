package com.omar.retromp3recorder.utils.domain.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

open class PublishSubjectRepo<T : Any>(replay: Int = 0) : MutableSharedFlow<T> {
    private val state = MutableSharedFlow<T>(
        replay = replay,
        onBufferOverflow = if (replay > 0) BufferOverflow.DROP_OLDEST else BufferOverflow.SUSPEND
    )

    open override suspend fun emit(value: T) {
        state.emit(value)
    }

    open fun flow(): Flow<T> = state

    suspend fun first() = flow().first()

    override val replayCache: List<T>
        get() = state.replayCache

    override val subscriptionCount: StateFlow<Int>
        get() = state.subscriptionCount

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        state.collect(collector)
    }

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() {
        state.resetReplayCache()
    }

    override fun tryEmit(value: T): Boolean {
        return state.tryEmit(value)
    }

}
