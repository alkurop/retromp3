package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.waveform.RecordWavetableMapper
import com.omar.retromp3recorder.bl.waveform.WavetableSummer
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.FutureFileWrapper
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.JoinedProgressRepo
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class JoinedProgressMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val currentFileRepo: CurrentFileRepo,
    private val joinedProgressRepo: JoinedProgressRepo,
    private val playerProgressRepo: PlayerProgressRepo,
    private val recorderWavetableMapper: RecordWavetableMapper,
    private val scheduler: Scheduler
) {
    fun observe(): Completable = Observable
        .merge(
            audioStateMapper.observe().ofType(AudioState.Seek_Paused::class.java).switchMap {
                Observable.combineLatest(
                    currentFileRepo.observe(),
                    playerProgressRepo.observe()
                ) { currentFile, playerProgress ->
                    val progress = playerProgress.value
                    if (progress != null && currentFile.value is ExistingFileWrapper) {
                        val file = (currentFile.value as ExistingFileWrapper)
                        JoinedProgress.PlayerProgressShown(
                            Shell(progress),
                            file.wavetable!!
                        )
                    } else
                        JoinedProgress.Hidden
                }
            },
            audioStateMapper.observe().ofType(AudioState.Playing::class.java).switchMap {
                Observable.combineLatest(
                    currentFileRepo.observe(),
                    playerProgressRepo.observe()
                ) { currentFile, playerProgress ->
                    val progress = playerProgress.value
                    if (progress != null && currentFile.value is ExistingFileWrapper) {
                        val file = (currentFile.value as ExistingFileWrapper)
                        JoinedProgress.PlayerProgressShown(
                            Shell(progress),
                            file.wavetable
                        )
                    } else
                        JoinedProgress.Hidden
                }
            },
            audioStateMapper.observe().ofType(AudioState.Idle::class.java).switchMap {
                Observable.combineLatest(
                    currentFileRepo.observe(),
                    playerProgressRepo.observe()
                ) { currentFile, playerProgress ->
                    val progress = playerProgress.value
                    if (progress != null && currentFile.value is ExistingFileWrapper) {
                        val file = (currentFile.value as ExistingFileWrapper)
                        JoinedProgress.PlayerProgressShown(
                            Shell(progress),
                            file.wavetable
                        )
                    } else if (currentFile.value is FutureFileWrapper) {
                        JoinedProgress.PlayerProgressShown(
                            Shell(progress),
                            null
                        )
                    } else JoinedProgress.Hidden
                }
            },
            audioStateMapper.observe().ofType(AudioState.Recording::class.java).switchMap {
                val rate = Mp3VoiceRecorder.WaveTableSampleRate._100
                recorderWavetableMapper.observe()
                    .takeUntil(audioStateMapper.observe().ofType(AudioState.Idle::class.java))
                    .scan(WavetableSummer(), WavetableSummer.displayScanFunction)
                    .map {
                        val wavetable = it.toWaveTable(false)
                        JoinedProgress.RecorderProgressShown(
                            it.getProgress() * rate.value,
                            wavetable
                        )
                    }
            }
        )
        .flatMapCompletable {
            Completable.fromAction {
                joinedProgressRepo.onNext(it)
            }
        }
        .subscribeOn(scheduler)
}

