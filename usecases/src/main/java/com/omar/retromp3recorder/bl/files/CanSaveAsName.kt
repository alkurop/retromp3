package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.FileRenamer
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CanSaveAsName @Inject constructor(
    private val fileRenamer: FileRenamer
) {
    fun execute(path: String, canRenameFileRepo: BehaviorSubjectRepo<Boolean>): Completable =

        Completable.fromAction {
            canRenameFileRepo.onNext(fileRenamer.exists(path).not())
        }
}
