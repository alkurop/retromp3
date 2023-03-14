package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.waveform.WaveformScanner
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.io.audiotransformer.AudioCropper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.utils.domain.AmplitudaDealer
import com.omar.retromp3recorder.utils.domain.FileLister
import com.omar.retromp3recorder.utils.domain.Mp3TagsEditor
import com.omar.retromp3recorder.utils.platform.Optional
import com.omar.retromp3recorder.utils.platform.toOptional
import javax.inject.Inject


class CropUC @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val appDatabase: AppDatabase,
    private val audioCropper: AudioCropper,
    private val gatherCropRequestUC: GatherCropRequestUC,
    private val fileLister: FileLister,
    private val mp3TagsEditor: Mp3TagsEditor,
    private val waveformScanner: WaveformScanner,
) {
    suspend fun execute(nameSuggestion: NewNameSuggestion): Optional<ExistingFileWrapper> {

        val request = gatherCropRequestUC.execute(nameSuggestion)

        val cropResponse = audioCropper.crop(request)

        return if (cropResponse.isSuccess.not()) {
            Optional.empty()
        } else {
            val tags = mp3TagsEditor.getTags(request.original.path)
                .copy(title = request.newFileNameSuggestion.name)

            mp3TagsEditor.setTags(request.newFileNameSuggestion.path, tags)

            val discoveredFile = fileLister.discoverFile(request.newFileNameSuggestion.path)

            val fileWithWaveform = waveformScanner.execute(
                discoveredFile,
                amplitudaDealer.createAmplituda()
            ).blockingGet()

            val id = appDatabase.fileEntityDao()
                .insert(fileWithWaveform.toDatabaseEntity())

            fileWithWaveform.copy(id = id).toOptional()
        }
    }
}



