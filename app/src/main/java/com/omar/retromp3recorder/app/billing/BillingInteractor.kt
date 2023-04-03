package com.omar.retromp3recorder.app.billing

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.billing.actions.BuyProductUC
import com.omar.retromp3recorder.bl.billing.actions.ConsumeProductUC
import com.omar.retromp3recorder.domain.*
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.storage.repo.global.BillingRequestEventBus
import com.omar.retromp3recorder.storage.repo.global.BillingResultEventBus
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class BillingInteractor @Inject constructor(
    private val billingRequestEventBus: BillingRequestEventBus,
    private val billingResultEventBus: BillingResultEventBus,
    private val consumeProductUC: ConsumeProductUC,
    private val billing: Billing,
    private val buyProductUC: BuyProductUC,
    private val scopeJobWrapper: ScopeJobWrapper,
    dispatcher: CoroutineDispatcher
) : Interactor<BillingRequest, Unit>(dispatcher) {
    suspend fun setup() {
        processIO(billingRequestEventBus.flow()).collect()
    }

    override fun listRepos(): List<Flow<Unit>> = emptyList()

    override suspend fun FlowCollector<Unit>.launchUseCase(input: BillingRequest) {
        scopeJobWrapper.launch {
            executeBillingRequest {
                when (input) {
                    BillingRequest.CropProductBuyRequest ->
                        buyProductUC.execute(ProductId.CROP_10).also {
                            it.onSuccess { billing.disconnect() }
                        }.map { data ->
                            BillingResponse.CropProductBuyResponse(data)
                        }
                    BillingRequest.CropProductConsumeRequest -> consumeProductUC.execute(
                        ProductId.CROP_10
                    ).map { BillingResponse.CropProductConsumeResponse(it) }
                }
            }
        }
    }

    private suspend fun executeBillingRequest(action: suspend () -> Result<BillingResponse>) =
        billingResultEventBus.emit(action())
}
