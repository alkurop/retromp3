package com.omar.retromp3recorder.bl.waveform

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.LoadingRepo
import com.omar.retromp3recorder.utils.AmplitudaDealer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import linc.com.amplituda.Amplituda
import javax.inject.Inject

class WaveformScanner @Inject constructor() {
    fun execute(file: ExistingFileWrapper, amplituda: Amplituda): Single<ExistingFileWrapper> {
        return Single.just(file)
    }
}