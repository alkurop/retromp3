package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.io.audiotransformer.AudioCropper
import com.omar.retromp3recorder.utils.DirPathProvider
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CropUC @Inject constructor(
    private val audioCropper: AudioCropper,
    private val dirPathProvider: DirPathProvider
) {
    fun execute(): Completable = Completable.complete()
}
