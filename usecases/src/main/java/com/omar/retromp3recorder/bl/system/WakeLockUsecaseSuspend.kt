package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.utils.domain.ServiceDealer
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WakeLockUsecaseSuspend @Inject constructor(
    private val serviceDealer: ServiceDealer
) {
    private var coroutineContext: Job = Job()

    suspend fun execute() {
        coroutineContext.cancelAndJoin()
        coroutineContext = Job()
        withContext(coroutineContext) {
            serviceDealer.startWakelockService()
        }
    }
}
