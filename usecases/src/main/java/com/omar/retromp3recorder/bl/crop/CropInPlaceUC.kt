package com.omar.retromp3recorder.bl.crop

import com.omar.retromp3recorder.utils.platform.toOptional
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CropInPlaceUC @Inject constructor(
    private val cropOutsideUC: CropOutsideUC,
    private val currentFileRepo: CurrentFileRepo,
) {
    fun execute(nameSuggestion: com.omar.retromp3recorder.domain.NewNameSuggestion): Completable =
        cropOutsideUC
            .execute(nameSuggestion)
            .flatMapCompletable {
                Completable.fromAction {
                    it.value?.let { file ->
                        currentFileRepo.tryNext(file.toOptional())
                    }
                }
            }
}
