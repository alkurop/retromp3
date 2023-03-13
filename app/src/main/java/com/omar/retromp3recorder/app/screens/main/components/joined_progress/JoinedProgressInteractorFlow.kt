package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import com.omar.retromp3recorder.bl.audio.AudioSeekFinishUC
import com.omar.retromp3recorder.bl.audio.AudioSeekPauseUC
import com.omar.retromp3recorder.bl.audio.AudioSeekProgressUC
import com.omar.retromp3recorder.bl.audio.JoinedProgressMapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class JoinedProgressInteractorFlow @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val audioSeekProgressUC: AudioSeekProgressUC,
    private val audioSeekPauseUC: AudioSeekPauseUC,
    private val audioSeekFinishUC: AudioSeekFinishUC,
    private val joinedProgressRepo: JoinedProgressMapper,
    dispatcher: CoroutineDispatcher,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()
    fun processIO(upstream: Flow<JoinedProgressView.In>): Flow<JoinedProgressView.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge()
    }

    private fun Flow<JoinedProgressView.In>.processInputs(): Flow<JoinedProgressView.Output> {
        return this.transform { event ->
            launch {
                when (event) {
                    is JoinedProgressView.In.SeekToPosition -> {
                        audioSeekProgressUC.execute(event.position).blockingAwait()
                    }
                    is JoinedProgressView.In.SeekingStarted -> {
                        audioSeekPauseUC.execute().blockingAwait()
                    }
                    is JoinedProgressView.In.SeekingFinished -> {
                        audioSeekFinishUC.execute().blockingAwait()
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
