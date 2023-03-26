package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.platform.FileRenamer
import com.omar.retromp3recorder.utils.platform.Mp3TagsEditor
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.repo.first
import javax.inject.Inject

class RenameFileUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currentFileRepo: CurrentFileRepo,
    private val fileRenamer: FileRenamer,
    private val mp3TagsEditor: Mp3TagsEditor,
) {
    suspend fun execute(newFileName: String) {
        val file = currentFileRepo.first()

        val fileWrapper = requireNotNull(file.value as? ExistingFileWrapper) {
            "Current file is not and existing file ${file.value}"
        }

        val newPath = fileRenamer.renameFile(fileWrapper, newFileName)

        mp3TagsEditor.getTags(newPath)
            ?.copy(title = mp3TagsEditor.getFilenameFromPath(newFileName))
            ?.let { tags ->
                mp3TagsEditor.setTags(newPath, tags)
            }

        val copy = fileWrapper.copy(
            path = newPath,
            modifiedTimestamp = System.currentTimeMillis()
        )

        appDatabase.fileEntityDao().updateItem(copy.toDatabaseEntity())
        currentFileRepo.emit(Optional(copy))
    }
}
