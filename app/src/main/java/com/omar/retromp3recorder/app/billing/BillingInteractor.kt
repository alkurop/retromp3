package com.omar.retromp3recorder.app.billing

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.bl.billing.actions.BuyProductUC
import com.omar.retromp3recorder.bl.billing.actions.ConsumeProductUC
import com.omar.retromp3recorder.domain.BillingRequest
import com.omar.retromp3recorder.domain.BillingResponse
import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.io.billing.Billing
import com.omar.retromp3recorder.storage.repo.global.BillingRequestEventBus
import com.omar.retromp3recorder.storage.repo.global.BillingResultEventBus
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
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
    private val toastRepo: ToastRepo,
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
                            it.onSuccess {
                                billing.disconnect()
                                toastRepo.emit(Stringer(R.string.thank_you_toast))
                            }
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
