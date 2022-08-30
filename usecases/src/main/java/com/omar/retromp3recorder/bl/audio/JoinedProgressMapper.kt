package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.files.CurrentFileMapper
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.storage.repo.JoinedProgressRepo
import com.omar.retromp3recorder.storage.repo.WavetableSampleRateRepo
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class JoinedProgressMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val currentFileMapper: CurrentFileMapper,
    private val joinedProgressRepo: JoinedProgressRepo,
    private val playerProgressRepo: PlayerProgressRepo,
    private val recorderWavetableMapper: RecordWavetableMapper,
    private val wavetableSampleRateRepo: WavetableSampleRateRepo,
    private val scheduler: Scheduler
) {
    fun observe(): Completable = Observable
        .merge(
            audioStateMapper.observe().ofType(AudioState.Seek_Paused::class.java).switchMap {
                Observable.combineLatest(
                    currentFileMapper.observe(),
                    playerProgressRepo.observe()
                ) { currentFile, playerProgress ->
                    val progress = playerProgress.value
                    if (progress != null && currentFile.value is ExistingFileWrapper) {
                        val file = (currentFile.value as ExistingFileWrapper)
                        JoinedProgress.PlayerProgressShown(
                            Shell(progress),
                            Shell(file.wavetable!!)
                        )
                    } else
                        JoinedProgress.Hidden
                }
            },
            audioStateMapper.observe().ofType(AudioState.Playing::class.java).switchMap {
                Observable.combineLatest(
                    currentFileMapper.observe(),
                    playerProgressRepo.observe()
                ) { currentFile, playerProgress ->
                    val progress = playerProgress.value
                    if (progress != null && currentFile.value is ExistingFileWrapper) {
                        val file = (currentFile.value as ExistingFileWrapper)
                        JoinedProgress.PlayerProgressShown(
                            Shell(progress),
                            Shell(file.wavetable!!)
                        )
                    } else
                        JoinedProgress.Hidden
                }
            },
            audioStateMapper.observe().ofType(AudioState.Idle::class.java).switchMap {
                Observable.combineLatest(
                    currentFileMapper.observe(),
                    playerProgressRepo.observe()
                ) { currentFile, playerProgress ->
                    val progress = playerProgress.value
                    if (progress != null && currentFile.value is ExistingFileWrapper) {
                        val file = (currentFile.value as ExistingFileWrapper)
                        JoinedProgress.PlayerProgressShown(
                            Shell(progress),
                            Shell(file.wavetable!!)
                        )
                    } else
                        JoinedProgress.Hidden
                }
            },
            audioStateMapper.observe().ofType(AudioState.Recording::class.java).switchMap {
                recorderWavetableMapper.observe()
                    .takeUntil(audioStateMapper.observe().ofType(AudioState.Idle::class.java))
                    .scan(emptyList<Byte>()) { list, newItem ->
                        list + newItem
                    }
                    .map {
                        val waveTableSampleRate =
                            wavetableSampleRateRepo.observe().blockingFirst().value
                        JoinedProgress.RecorderProgressShown(
                            it.size * waveTableSampleRate.toLong(),
                            Wavetable(it.toByteArray(), waveTableSampleRate)
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

