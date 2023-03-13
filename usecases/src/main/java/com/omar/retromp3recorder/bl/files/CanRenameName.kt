package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.common.StateFlowRepo
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.FileRenamer
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CanRenameName @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val fileRenamer: FileRenamer
) {
    fun execute(
        path: String,
        canRenameFileRepo: StateFlowRepo<Pair<Boolean, String?>>
    ): Completable =
        currentFileRepo
            .takeSingle()
            .flatMapCompletable { optional ->
                val fileWrapper = optional.value
                Completable.fromAction {
                    canRenameFileRepo.onNext(false to null)
                    val canRename =
                        if (path.isNotEmpty() && fileWrapper != null && fileWrapper is ExistingFileWrapper)
                            fileRenamer.canRename(fileWrapper, path) else false
                    canRenameFileRepo.onNext(canRename to path)
                }
            }
}
