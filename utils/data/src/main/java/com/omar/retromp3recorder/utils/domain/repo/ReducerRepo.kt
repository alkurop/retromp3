package com.omar.retromp3recorder.utils.domain.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

open class ReducerRepo<In : Any, State : Any>(
    init: State,
    private val reducer: State.(In) -> State
) : Repo<In, State> {
    private val stateKeeper = MutableStateFlow(init)

    override suspend fun emit(input: In) {
        val prev = stateKeeper.value
        val next = prev.reducer(input)
        stateKeeper.value = next
    }

    override fun flow(): Flow<State> = stateKeeper

}
