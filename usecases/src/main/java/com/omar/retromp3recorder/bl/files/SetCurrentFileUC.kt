package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SetCurrentFileUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(file: ExistingFileWrapper): Completable {
        return Completable.fromAction {
            currentFileRepo.onNext(Optional(file))
        }
    }
}
