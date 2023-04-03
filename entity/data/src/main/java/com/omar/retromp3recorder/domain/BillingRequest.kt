package com.omar.retromp3recorder.domain

sealed interface BillingRequest {
    object CropProductBuyRequest : BillingRequest
    object CropProductConsumeRequest : BillingRequest
}

sealed interface BillingResponse {
    data class CropProductBuyResponse(val result: PurchaseData) : BillingResponse

    data class CropProductConsumeResponse(val result: Unit) : BillingResponse
}

