package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.toOptional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HasPlayableFileMapperFlow @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
) {
    fun flow(): Flow<Optional<ExistingFileWrapper>> {
        return currentFileRepo.flow().map { currentFile ->
            val existingFile = (currentFile.value as? ExistingFileWrapper)
            if ((existingFile?.length ?: 0) == 0L) Optional.empty() else existingFile.toOptional()
        }
    }
}
