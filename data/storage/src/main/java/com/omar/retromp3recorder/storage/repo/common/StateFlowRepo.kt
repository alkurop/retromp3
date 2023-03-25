package com.omar.retromp3recorder.storage.repo.common

import com.omar.retromp3recorder.utils.domain.takeObservableOne
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
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

    open fun tryNext(next: T) {
        flow.tryEmit(next)
    }

    suspend fun emit(next: T) {
        flow.emit(next)
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "RxJava is deprecated",
        replaceWith = ReplaceWith(
            "observeFlow()"
        )
    )
    open fun observe(): Observable<T> = flow.asObservable()

    fun flow(): Flow<T> = flow

    suspend fun first(): T = flow.first()

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "RxJava is deprecated",
        replaceWith = ReplaceWith(
            "takeOneFlow()"
        )
    )
    fun takeSingle(): Single<T> = flow.asObservable().takeObservableOne()


}
