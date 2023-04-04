package com.omar.retromp3recorder.io.billing.connection

import com.omar.retromp3recorder.io.billing.mapping.RequestMapper
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class ConnectUC @Inject constructor(
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(connection: BillingConnection): Result<Unit> {
        return withContext(scopeJobWrapper.coroutineContext) {
            val flow = channelFlow {
                var job: Job? = null
                job = launch {
                    connection.connectionFlow.collect { item ->
                        when (item) {
                            BillingConnectionState.Loading -> {/*ignore */ }
                            BillingConnectionState.Connected -> {
                                send(Result.success(Unit))
                                connection.subscribeToMessages(RequestMapper.buildMessageParams()) {
                                    Timber.d("BILLING massage result $it")
                                }
                            }
                            is BillingConnectionState.Disconnected -> send(Result.failure(item.cause))
                        }

                        if (item != BillingConnectionState.Loading) {
                            close()
                            job?.cancel()
                        }
                    }
                }
            }

            connection.connect()
            flow.first()
        }
    }
}
