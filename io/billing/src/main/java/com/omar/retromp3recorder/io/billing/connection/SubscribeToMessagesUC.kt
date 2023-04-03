package com.omar.retromp3recorder.io.billing.connection

import com.omar.retromp3recorder.io.billing.mapping.RequestMapper.buildMessageParams
import timber.log.Timber
import javax.inject.Inject

internal class SubscribeToMessagesUC @Inject constructor(
) {
    fun execute(connection: BillingConnection) {
        connection.subscribeToMessages(buildMessageParams()) {
            Timber.d("BILLING massage result $it")
        }
    }
}
