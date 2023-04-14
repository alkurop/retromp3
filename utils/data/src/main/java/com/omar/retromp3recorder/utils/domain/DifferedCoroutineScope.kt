package com.omar.retromp3recorder.utils.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
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
}
