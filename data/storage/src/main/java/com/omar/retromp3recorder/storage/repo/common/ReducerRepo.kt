package com.omar.retromp3recorder.storage.repo.common

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

open class ReducerRepo<In : Any, State : Any>(
    init: State,
    private val function: State.(In) -> State
) {
    private val stateKeeper = BehaviorSubject.createDefault(init)

    @Synchronized
    fun onNext(input: In) {
        val prev = stateKeeper.blockingFirst()
        val next = prev.function(input)
        stateKeeper.onNext(next)
    }

    open fun observe(): Observable<State> = stateKeeper
}
