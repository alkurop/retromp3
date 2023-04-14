package com.omar.retromp3recorder.utils.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlin.coroutines.CoroutineContext

class DifferedCoroutineScope(
    private val dispatcher: CoroutineDispatcher
) : CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatcher + job

    fun cancel(){
        job.cancel()
        job = Job()
    }

    @Suppress("UNUSED")
    suspend fun cancelAndJoin() {
        job.cancelAndJoin()
        job = Job()
    }
}
