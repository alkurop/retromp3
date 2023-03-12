package com.omar.retromp3recorder.storage.repo.common

import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject

open class PublishSubjectRepo<T : Any>() {
    private val publishSubject: PublishSubject<T> = PublishSubject.create()

    open fun onNext(next: T) {
        publishSubject.onNext(next)
    }

    fun observe(): Observable<T> = publishSubject

    fun takeOne(): Single<T> = publishSubject.takeOne()
}
