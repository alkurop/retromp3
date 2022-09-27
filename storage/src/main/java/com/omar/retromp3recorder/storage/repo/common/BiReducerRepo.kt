package com.omar.retromp3recorder.storage.repo.common

import io.reactivex.rxjava3.core.Observable


abstract class BiReducerRepo<T1 : Any, T2 : Any>(
    private val observable: Observable<T2>,
    d: T1? = null
) :
    BehaviorSubjectRepo<T1>(d) {
    override fun observe(): Observable<T1> {
        return Observable.combineLatest(
            observable,
            super.observe(), reducer
        )
    }

    abstract val reducer: (T2, T1) -> T1
}
