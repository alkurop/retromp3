package com.omar.retromp3recorder.utils.platform

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T : Any> Flow<Shell<T>>.shellUnwrap(): Flow<T> = this.map {
    it.ghost
}.filterNotNull()

