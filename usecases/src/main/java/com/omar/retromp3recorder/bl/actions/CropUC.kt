package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.waveform.WaveformScanner
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.domain.platform.Optional
import com.omar.retromp3recorder.domain.platform.toOptional
import com.omar.retromp3recorder.io.audiotransformer.AudioCropper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.utils.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class CropUC @Inject constructor(
    private val amplitudaDealer: AmplitudaDealer,
    private val appDatabase: AppDatabase,
    private val audioCropper: AudioCropper,
    private val gatherCropRequestUC: GatherCropRequestUC,
    private val fileLister: FileLister,
    private val mp3TagsEditor: Mp3TagsEditor,
    private val waveformScanner: WaveformScanner,
    private val scheduler: Scheduler
) {
    fun execute(nameSuggestion: NewNameSuggestion): Single<Optional<ExistingFileWrapper>> =
        gatherCropRequestUC
            .execute(nameSuggestion)
            .flatMap { request ->
                Single
                    .fromCallable {
                        audioCropper.crop(request)
                    }
                    .flatMap { cropResponse ->
                        if (cropResponse.isSuccess.not()) {
                            Single.just(Optional.empty())
                        } else
                            Completable
                                .fromAction {
                                    val tags = mp3TagsEditor.getTags(request.original.path)
                                        .copy(title = request.newFileNameSuggestion.name)
                                    mp3TagsEditor.setTags(request.newFileNameSuggestion.path, tags)
                                }
                                .andThen(
                                    Single
                                        .fromCallable {
                                            fileLister.discoverFile(request.newFileNameSuggestion.path)
                                        })
                                .flatMap {
                                    waveformScanner.execute(
                                        it,
                                        amplitudaDealer.createAmplituda()
                                    )
                                }
                                .flatMap { fileWrapper ->
                                    Single.fromCallable {
                                        val id = appDatabase.fileEntityDao()
                                            .insert(fileWrapper.toDatabaseEntity())
                                        fileWrapper.copy(id = id).toOptional()
                                    }
                                }
                    }
            }
            .subscribeOn(scheduler)
}



