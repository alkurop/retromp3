package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.waveform.WaveformScannerSuspend
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.io.audiotransformer.AudioCropper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.utils.domain.Optional
import com.omar.retromp3recorder.utils.domain.toOptional
import com.omar.retromp3recorder.utils.platform.AmplitudaDealer
import com.omar.retromp3recorder.utils.platform.FileLister
import com.omar.retromp3recorder.utils.platform.Mp3TagsEditor
import javax.inject.Inject


class CropUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val audioCropper: AudioCropper,
    private val gatherCropRequestUC: GatherCropRequestUC,
    private val fileLister: FileLister,
    private val mp3TagsEditor: Mp3TagsEditor,
    private val waveformScanner: WaveformScannerSuspend,
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): Optional<ExistingFileWrapper> {

        val request = gatherCropRequestUC.execute(nameSuggestion)

        val cropResponse = audioCropper.crop(request)

        return if (cropResponse.isSuccess.not()) {
            Optional.empty()
        } else {
            mp3TagsEditor.getTags(request.original.path)
                ?.copy(title = request.newFileNameSuggestion.name)
                ?.let { tags ->
                    mp3TagsEditor.setTags(request.newFileNameSuggestion.path, tags)
                }

            val discoveredFile = fileLister.discoverFile(request.newFileNameSuggestion.path)

            val fileWithWaveform = waveformScanner.execute(discoveredFile)

            val id = appDatabase.fileEntityDao().insert(fileWithWaveform.toDatabaseEntity())

            fileWithWaveform.copy(id = id).toOptional()
        }
    }
}



