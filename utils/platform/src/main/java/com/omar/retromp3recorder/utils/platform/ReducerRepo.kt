package com.omar.retromp3recorder.utils.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

open class ReducerRepo<In : Any, State : Any>(
    init: State,
    private val reducer: State.(In) -> State
) {
    private val stateKeeper = MutableStateFlow(init)

    @Synchronized
   open fun emit(input: In) {
        val prev = stateKeeper.value
        val next = prev.reducer(input)
        stateKeeper.value = next
    }

    open fun flow(): Flow<State> = stateKeeper

}
