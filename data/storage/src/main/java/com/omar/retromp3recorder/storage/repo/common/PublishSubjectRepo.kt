package com.omar.retromp3recorder.storage.repo.common

import com.omar.retromp3recorder.utils.domain.takeObservableOne
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.rx3.asObservable

open class PublishSubjectRepo<T : Any> {
    private val state = MutableSharedFlow<T>(
        replay = 0
    )

    open fun onNext(next: T) {
        state.tryEmit(next)
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "RxJava is deprecated",
        replaceWith = ReplaceWith(
            "observeFlow()"
        )
    )
    open fun observe(): Observable<T> = state.asObservable()

    fun flow(): Flow<T> = state

    suspend fun first(): T = state.first()

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "RxJava is deprecated",
        replaceWith = ReplaceWith(
            "takeOneFlow()"
        )
    )
    fun takeObservableOne(): Single<T> = state.asObservable().takeObservableOne()
}
