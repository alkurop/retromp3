package com.omar.retromp3recorder.storage.repo.common

import com.omar.retromp3recorder.utils.domain.takeOne
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.rx3.asObservable

open class PublishSubjectRepo<T : Any> {
    private val state = MutableSharedFlow<T>(
        replay = 0
    )

    open fun onNext(next: T) {
        state.tryEmit(next)
    }

    fun observe(): Observable<T> = state.asObservable()

    fun takeOne(): Single<T> = state.asObservable().takeOne()
}
