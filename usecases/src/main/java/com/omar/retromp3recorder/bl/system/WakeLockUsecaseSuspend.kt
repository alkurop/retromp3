package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.utils.domain.ServiceDealer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WakeLockUsecaseSuspend @Inject constructor(
    private val serviceDealer: ServiceDealer
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()

    suspend fun execute() {
        withContext(coroutineContext) {
            serviceDealer.startWakelockService()
        }
    }
}
