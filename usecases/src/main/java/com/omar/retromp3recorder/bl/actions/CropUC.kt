package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.waveform.WaveformScannerSuspend
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.domain.mapError
import com.omar.retromp3recorder.domain.toResult
import com.omar.retromp3recorder.io.audiotransformer.AudioCropper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.platform.FileLister
import com.omar.retromp3recorder.utils.platform.Mp3TagsEditor
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CropUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val audioCropper: AudioCropper,
    private val gatherCropRequestUC: GatherCropRequestUC,
    private val fileLister: FileLister,
    private val mp3TagsEditor: Mp3TagsEditor,
    private val waveformScanner: WaveformScannerSuspend,
    private val scopeJobWrapper: ScopeJobWrapper
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): Result<ExistingFileWrapper> {

        return withContext(scopeJobWrapper.coroutineContext) {
            val request = gatherCropRequestUC.execute(nameSuggestion)
            val cropResponse = audioCropper.crop(request)
            if (cropResponse.isSuccess.not()) {
                cropResponse.mapError()
            } else {
                mp3TagsEditor.getTags(request.original.path)
                    ?.copy(title = request.newFileNameSuggestion.name)
                    ?.let { tags ->
                        mp3TagsEditor.setTags(request.newFileNameSuggestion.path, tags)
                    }

                val discoveredFile = fileLister.discoverFile(request.newFileNameSuggestion.path)

                val fileWithWaveform = waveformScanner.execute(discoveredFile)

                val id = appDatabase.fileEntityDao().insert(fileWithWaveform.toDatabaseEntity())

                fileWithWaveform.copy(id = id).toResult()
            }
        }
    }
}
