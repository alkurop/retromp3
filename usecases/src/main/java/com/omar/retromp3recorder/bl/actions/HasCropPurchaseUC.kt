package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.storage.repo.global.CropProductRepo
import timber.log.Timber
import javax.inject.Inject

class HasCropPurchaseUC @Inject constructor(
    private val cropProductRepo: CropProductRepo,
) {
    suspend fun execute(): Boolean {
        val cropProductCount = cropProductRepo.first()
        Timber.d("BILLING Crop purchase count $cropProductCount")
        return cropProductCount > 0
    }
}
