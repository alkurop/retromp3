package com.omar.retromp3recorder.bl.billing

import com.omar.retromp3recorder.domain.BillingRequest
import com.omar.retromp3recorder.domain.BillingResponse
import com.omar.retromp3recorder.storage.repo.global.BillingRequestEventBus
import com.omar.retromp3recorder.storage.repo.global.BillingResultEventBus
import com.omar.retromp3recorder.utils.domain.toResult
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class BillingBinderUC @Inject constructor(
    private val billingRequestEventBus: BillingRequestEventBus,
    private val billingResultEventBus: BillingResultEventBus,
) {
    suspend fun <T : BillingResponse> execute(request: BillingRequest): Result<T> {
        billingRequestEventBus.emit(request)
        val result = billingResultEventBus.first()
        return result as? Result<T>
            ?: Error("Billing request type $request does not match result type $result").toResult()
    }
}
