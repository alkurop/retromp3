package com.omar.retromp3recorder.app.screens.main.components.joined_progress

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekFinishUC
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekPauseUC
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekProgressUC
import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapperFlow
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JoinedProgressInteractorFlow @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val audioSeekProgressUC: AudioSeekProgressUC,
    private val audioSeekPauseUC: AudioSeekPauseUC,
    private val audioSeekFinishUC: AudioSeekFinishUC,
    private val joinedProgressRepo: JoinedProgressMapperFlow,
    dispatcher: CoroutineDispatcher,
) : Interactor<JoinedProgressView.In, JoinedProgressView.Output>(dispatcher) {

    override fun listRepos(): List<Flow<JoinedProgressView.Output>> {
        return listOf(
            joinedProgressRepo.flow().map {
                JoinedProgressView.Output.JoinedProgressChanged(it)
            },
            currentFileRepo.flow().map { file ->
                JoinedProgressView.Output.CurrentFileChanged(
                    file.value
                )
            }
        )
    }

    override suspend fun FlowCollector<JoinedProgressView.Output>.launchUseCase(input: JoinedProgressView.In) {
        when (input) {
            is JoinedProgressView.In.SeekToPosition -> {
                audioSeekProgressUC.execute(input.position)
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
