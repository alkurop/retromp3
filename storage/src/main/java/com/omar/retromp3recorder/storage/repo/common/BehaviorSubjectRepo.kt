package com.omar.retromp3recorder.storage.repo.common

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

open class BehaviorSubjectRepo<T : Any>(default: T? = null) {
    private val behaviorSubject: BehaviorSubject<T> =
        if (default == null) BehaviorSubject.create()
        else BehaviorSubject.createDefault(default)

    fun onNext(next: T) {
        behaviorSubject.onNext(next)
    }

    fun observe(): Observable<T> = behaviorSubject

    fun hasNext(): Boolean = behaviorSubject.hasValue()
}