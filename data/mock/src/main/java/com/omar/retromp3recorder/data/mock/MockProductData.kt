package com.omar.retromp3recorder.data.mock

import com.omar.retromp3recorder.domain.ProductId
import com.omar.retromp3recorder.domain.PurchaseData
import java.util.*

object MockProductData {
    fun giveMockProduct(
        productId: ProductId = ProductId.CROP_10,
        quantity: Int = 1,
        isAcknowledged: Boolean = true
    ) = PurchaseData(
        productId = productId,
        purchaseToken = Random().nextLong().toString(),
        quantity = quantity,
        isAcknowledged = isAcknowledged
    )
}
