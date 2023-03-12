package com.omar.retromp3recorder.storage.repo.common

import com.omar.retromp3recorder.utils.domain.takeOne
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.rx3.asObservable


open class StateFlowRepo<T : Any>(default: T? = null) {
    private val flow = MutableSharedFlow<T>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        if (default != null) {
            flow.tryEmit(default)
        }
    }

    open fun onNext(next: T) {
        flow.tryEmit(next)
    }

    open fun observe(): Observable<T> = flow.asObservable()

    fun takeOne(): Single<T> = flow.take(1).asObservable().takeOne()
}
