package com.omar.retromp3recorder.bl.billing

import com.omar.retromp3recorder.io.billing.Billing
import javax.inject.Inject

class DisconnectBillingUC @Inject constructor(
    private val billing: Billing,
) {
    fun execute() {
        billing.disconnect()
    }
}
