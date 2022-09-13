package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import com.omar.retromp3recorder.utils.*
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveRecordingWithWavetableUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val wavetableRepo: WavetableRepo,
    private val fileLister: FileLister,
    private val saveMp3TagsUC: SaveMp3TagsUC
) {
    fun execute(): Completable =
        wavetableRepo.observe()
            .takeOne()
            .flatMapCompletable { shell ->
                val (path, wave) = shell
                saveMp3TagsUC.execute(path).andThen(
                    Completable
                        .fromAction {
                            val fileEntityDao = appDatabase.fileEntityDao()
                            val newItem = fileLister.discoverFile(path)
                                .copy(wavetable = wave, length = fileLister.discoverLength(path))
                            fileEntityDao.insert(listOf(newItem.toDatabaseEntity()))
                        })
            }
}

class SaveMp3TagsUC @Inject constructor(
    private val mp3TagsEditor: Mp3TagsEditor,
    private val recordingTagsDefaultProvider: RecordingTagsDefaultProvider
) {
    fun execute(filepath: String): Completable = Completable.fromAction {
        mp3TagsEditor.setTags(
            filepath,
            recordingTagsDefaultProvider.provideDefaults().copy(
                title = mp3TagsEditor.getFilenameFromPath(filepath)
            )
        )
    }
}