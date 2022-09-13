package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.FileEmptyChecker
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class HasPlayableFileMapper @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val fileEmptyChecker: FileEmptyChecker,
) {
    fun observe(): Observable<Boolean> {
        return currentFileRepo.observe().map { currentFile ->
            val path = currentFile.value?.path
            path != null && fileEmptyChecker.isFileEmpty(path).not()
        }
    }
}