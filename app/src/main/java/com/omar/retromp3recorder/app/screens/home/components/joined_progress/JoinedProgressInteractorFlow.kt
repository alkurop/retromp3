package com.omar.retromp3recorder.app.screens.home.components.joined_progress

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekFinishUC
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekPauseUC
import com.omar.retromp3recorder.bl.audio.progress.AudioSeekProgressUC
import com.omar.retromp3recorder.bl.audio.progress.JoinedProgressMapper
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
    private val joinedProgressRepo: JoinedProgressMapper,
    dispatcher: CoroutineDispatcher,
) : Interactor<JoinedProgressContract.In, JoinedProgressContract.Output>(dispatcher) {

    override fun listRepos(): List<Flow<JoinedProgressContract.Output>> {
        return listOf(
            joinedProgressRepo.flow().map {
                JoinedProgressContract.Output.JoinedProgressChanged(it)
            },
            currentFileRepo.flow().map { file ->
                JoinedProgressContract.Output.CurrentFileChanged(
                    file.value
                )
            }
        )
    }

    override suspend fun FlowCollector<JoinedProgressContract.Output>.launchUseCase(input: JoinedProgressContract.In) {
        when (input) {
            is JoinedProgressContract.In.SeekToPosition -> {
                audioSeekProgressUC.execute(input.position)
            }
            is JoinedProgressContract.In.SeekingStarted -> {
                audioSeekPauseUC.execute()
            }
            is JoinedProgressContract.In.SeekingFinished -> {
                audioSeekFinishUC.execute()
            }
        }
    }
}
