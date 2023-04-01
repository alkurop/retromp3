package com.omar.retromp3recorder.bl.billing

import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.io.billing.BillingConnectionState
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConnectToBillingUC @Inject constructor(
    private val billing: Billing,
    private val scopeJobWrapper: ScopeJobWrapper,
) {
    suspend fun execute(): Result<Unit> {
        return withContext(scopeJobWrapper.coroutineContext) {
            billing.connect()
            val flow = channelFlow {
                var job: Job? = null
                job = launch {
                    billing.connectionFlow().collect { item ->
                        when (item) {
                            BillingConnectionState.Loading -> {
                                //ignore
                            }
                            BillingConnectionState.Connected -> send(Result.success(Unit))
                            is BillingConnectionState.Disconnected -> send(Result.failure(item.cause))
                        }

                        if (item != BillingConnectionState.Loading) {
                            close()
                            job?.cancel()
                        }
                    }
                }
            }
            flow.first()
        }
    }
}
