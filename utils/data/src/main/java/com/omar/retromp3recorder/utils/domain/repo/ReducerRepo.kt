package com.omar.retromp3recorder.utils.domain.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

open class ReducerRepo<In : Any, State : Any>(
    init: State,
    private val reducer: State.(In) -> State
) {
    private val stateKeeper = MutableStateFlow(init)

    suspend fun emit(input: In) {
        val prev = stateKeeper.value
        val next = prev.reducer(input)
        stateKeeper.emit(next)
    }

    open fun flow(): Flow<State> = stateKeeper

}

suspend fun <Out : Any> ReducerRepo<Out, *>.first() = flow().first()
