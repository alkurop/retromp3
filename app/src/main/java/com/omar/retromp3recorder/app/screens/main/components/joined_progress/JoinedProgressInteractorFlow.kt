package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import com.omar.retromp3recorder.bl.audio.AudioSeekFinishUC
import com.omar.retromp3recorder.bl.audio.AudioSeekPauseUC
import com.omar.retromp3recorder.bl.audio.AudioSeekProgressUC
import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject

@OptIn(FlowPreview::class)
class JoinedProgressInteractorFlow @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val audioSeekProgressUC: AudioSeekProgressUC,
    private val audioSeekPauseUC: AudioSeekPauseUC,
    private val audioSeekFinishUC: AudioSeekFinishUC,
    private val joinedProgressRepo: JoinedProgressMapper,
    private val dispatcher: CoroutineDispatcher,
) {
    fun processIO(upstream: Flow<JoinedProgressView.In>): Flow<JoinedProgressView.Output> {
        return listOf(
            listenToRepos(),
            upstream.processInputs(),
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<JoinedProgressView.In>.processInputs(): Flow<JoinedProgressView.Output> {
        return this.flatMapMerge { event ->
            flow {
                when (event) {
                    is JoinedProgressView.In.SeekToPosition -> {
                        audioSeekProgressUC.execute(event.position)
                    }
                    is JoinedProgressView.In.SeekingStarted -> {
                        audioSeekPauseUC.execute()
                    }
                    is JoinedProgressView.In.SeekingFinished -> {
                        audioSeekFinishUC.execute()
                    }
                }
            }
        }
    }

    private fun listenToRepos(): Flow<JoinedProgressView.Output> {
        return listOf(
            joinedProgressRepo.observe().asFlow().map {
                JoinedProgressView.Output.JoinedProgressChanged(it)
            },
            currentFileRepo.flow().map { file ->
                JoinedProgressView.Output.CurrentFileChanged(
                    file.value
                )
            }
        ).merge()
    }
}
