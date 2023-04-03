package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.storage.repo.global.CropProductRepo
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class HasCropPurchaseUC @Inject constructor(
    private val cropProductRepo: CropProductRepo,
    private val scopeJobWrapper: ScopeJobWrapper,
) {
    suspend fun execute(): Boolean {
        return withContext(scopeJobWrapper.coroutineContext) {
            val cropProductCount = cropProductRepo.first()
            Timber.d("BILLING Crop purchase count $cropProductCount")
            cropProductCount > 0
        }
    }
}
