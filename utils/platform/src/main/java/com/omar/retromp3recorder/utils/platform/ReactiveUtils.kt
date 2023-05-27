package com.omar.retromp3recorder.utils.platform

import com.github.alkurop.ghostinshell.Shell
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <T : Any> Flow<Shell<T>>.shellUnwrap(): Flow<T> = this.map {
    it.ghost
}.filterNotNull()


fun tickerFlow(millis: Long) = flow {
    while (true) {
        delay(millis)
        emit(Unit)
    }
}
