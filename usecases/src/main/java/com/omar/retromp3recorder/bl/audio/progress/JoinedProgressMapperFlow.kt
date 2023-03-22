package com.omar.retromp3recorder.bl.audio.progress

import com.omar.retromp3recorder.bl.waveform.RecordWavetableMapper
import com.omar.retromp3recorder.bl.waveform.WavetableSummer
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.domain.FutureFileWrapper
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class JoinedProgressMapperFlow @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val currentFileRepo: CurrentFileRepo,
    private val playerProgressRepo: PlayerProgressRepo,
    private val recorderWavetableMapper: RecordWavetableMapper,
    private val dispatcher: CoroutineDispatcher
) {

    fun flow(): Flow<JoinedProgress> {
        return listOf(
            flowNotRecording(),
            flowRecording(),
        ).merge().flowOn(dispatcher)
    }

    private fun flowNotRecording(): Flow<JoinedProgress> {
        return audioStateMapper.observe().asFlow().filter { it !is AudioState.Recording }
            .flatMapLatest {
                combine(
                    currentFileRepo.flow(),
                    playerProgressRepo.flow()
                ) { currentFile, playerProgress ->
                    val progress = playerProgress.value
                    if (progress != null && currentFile.value is ExistingFileWrapper) {
                        val file = (currentFile.value as ExistingFileWrapper)
                        JoinedProgress.PlayerProgressShown(
                            progress,
                            file.wavetable
                        )
                    } else if (currentFile.value is FutureFileWrapper) {
                        JoinedProgress.Intermediate
                    } else JoinedProgress.Hidden
                }
            }
    }

    private fun flowRecording(): Flow<JoinedProgress> {
        return audioStateMapper.observe().asFlow().filterIsInstance<AudioState.Recording>()
            .flatMapLatest {
                val rate = Mp3VoiceRecorder.WaveTableSampleRate._100
                recorderWavetableMapper.observe()
                    .asFlow()
                    .scan(WavetableSummer(), WavetableSummer.displayScanFunction)
                    .map {
                        val wavetable = it.toWaveTable(false)
                        JoinedProgress.RecorderProgressShown(
                            it.getProgress() * rate.value,
                            wavetable,
                        )
                    }
            }
    }
}

