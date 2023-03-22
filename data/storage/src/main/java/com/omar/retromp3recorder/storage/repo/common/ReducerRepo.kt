package com.omar.retromp3recorder.storage.repo.common

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.rx3.asObservable

open class ReducerRepo<In : Any, State : Any>(
    init: State,
    private val function: State.(In) -> State
) {
    private val stateKeeper = MutableStateFlow(init)

    @Synchronized
    fun emit(input: In) {
        val prev = stateKeeper.value
        val next = prev.function(input)
        stateKeeper.value = next
    }


    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "RxJava is deprecated",
        replaceWith = ReplaceWith(
            "observeFlow()"
        )
    )
    open fun observe(): Observable<State> = stateKeeper.asObservable()

    open fun flow(): Flow<State> = stateKeeper

}
