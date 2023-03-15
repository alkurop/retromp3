package com.omar.retromp3recorder.utils.domain

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T : Any> Observable<Shell<T>>.flatMapGhost(): Observable<T> = this.flatMap {
    val value = it.ghost
    if (value != null) Observable.just(value)
    else Observable.empty()
}

fun <In : Any, Out : Any> Scheduler.processIO(
    inputMapper: (Observable<In>) -> Completable,
    outputMapper: () -> Observable<Out>
): ObservableTransformer<In, Out> = ObservableTransformer { actions ->
    outputMapper().mergeWith(
        inputMapper(actions.observeOn(this)).subscribeOn(this)
    )
}

fun <T : Any> Observable<T>.takeObservableOne():Single<T> = this.take(1).singleOrError()
