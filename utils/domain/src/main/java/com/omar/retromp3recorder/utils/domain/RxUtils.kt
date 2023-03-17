package com.omar.retromp3recorder.utils.domain

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T : Any> Observable<Shell<T>>.shellUnwrap(): Observable<T> = this.flatMap {
    val value = it.ghost
    if (value != null) Observable.just(value)
    else Observable.empty()
}

fun <T : Any> Flow<Shell<T>>.shellUnwrap(): Flow<T> = this.map {
    it.ghost
}.filterNotNull()


fun <In : Any, Out : Any> Scheduler.processIO(
    inputMapper: (Observable<In>) -> Completable,
    outputMapper: () -> Observable<Out>
): ObservableTransformer<In, Out> = ObservableTransformer { actions ->
    outputMapper().mergeWith(
        inputMapper(actions.observeOn(this)).subscribeOn(this)
    )
}

fun <T : Any> Observable<T>.takeObservableOne(): Single<T> = this.take(1).singleOrError()
