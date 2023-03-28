package com.omar.retromp3recorder.utils.platform

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T : Any> Flow<Shell<T>>.shellUnwrap(): Flow<T> = this.map {
    it.ghost
}.filterNotNull()

fun <T> Flow<T>.chunked(maxSize: Int, intervalMillis: Long) = channelFlow {
    val buffer = mutableListOf<T>()
    var flushJob: Job? = null

    collect { value ->
        flushJob?.cancelAndJoin()
        buffer.add(value)

        if (buffer.size >= maxSize) {
            send(buffer.toList())
            buffer.clear()
        } else {
            flushJob = launch {
                delay(intervalMillis)
                if (buffer.isNotEmpty()) {
                    send(buffer.toList())
                    buffer.clear()
                }
            }
        }
    }

    flushJob?.cancelAndJoin()

    if (buffer.isNotEmpty()) {
        send(buffer.toList())
        buffer.clear()
    }
}
