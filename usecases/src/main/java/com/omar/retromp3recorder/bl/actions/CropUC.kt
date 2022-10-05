package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.waveform.WaveformScanner
import com.omar.retromp3recorder.io.audiotransformer.AudioCropper
import com.omar.retromp3recorder.io.audiotransformer.CropRequest
import com.omar.retromp3recorder.io.audiotransformer.CropResponse
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


//todo disable menu during recording and hide when no file present
//todo recalculate player counter when in range
class CropUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val audioCropper: AudioCropper,
    private val currentFileRepo: CurrentFileRepo,
    private val waveformScanner: WaveformScanner,

) {
    fun execute(request: CropRequest): Single<CropResponse> = Single
        .fromCallable {
            audioCropper.crop(request)
        }.flatMap {
            if (it.isSuccess.not()) {
                Single.just(it)
            } else Single.just(it)
        }
}



