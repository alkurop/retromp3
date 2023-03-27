package com.omar.retromp3recorder.utils.domain.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

open class PublishSubjectRepo<T : Any>(replay: Int = 0) {
    private val state = MutableSharedFlow<T>(
        replay = replay
    )

    open suspend fun emit(input: T) {
        state.emit(input)
    }

    open fun flow(): Flow<T> = state

}

suspend fun <Out : Any> PublishSubjectRepo<Out>.first() = flow().first()
