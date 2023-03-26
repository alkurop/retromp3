package com.omar.retromp3recorder.utils.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlin.coroutines.CoroutineContext

class ScopeJobWrapper(val dispatcher: CoroutineDispatcher) : CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatcher + job

    suspend fun cancelAndJoin() {
        job.cancelAndJoin()
        job = Job()
    }
}
