package com.omar.retromp3recorder.utils.platform.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

open class PublishSubjectRepo<T : Any>(replay: Int = 0) : Repo<T, T> {
    private val state = MutableSharedFlow<T>(
        replay = replay
    )

    override suspend fun emit(input: T) {
        state.tryEmit(input)
    }

    override fun flow(): Flow<T> = state

}
