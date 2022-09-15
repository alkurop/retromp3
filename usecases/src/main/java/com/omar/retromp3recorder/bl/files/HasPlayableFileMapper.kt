package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.toOptional
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class HasPlayableFileMapper @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
) {
    fun observe(): Observable<Optional<ExistingFileWrapper>> {
        return currentFileRepo.observe().map { currentFile ->
            val existingFile = (currentFile.value as? ExistingFileWrapper)
            if ((existingFile?.length ?: 0) == 0L) Optional.empty() else existingFile.toOptional()
        }
    }
}
