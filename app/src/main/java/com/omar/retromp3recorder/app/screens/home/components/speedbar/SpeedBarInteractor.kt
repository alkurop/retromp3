package com.omar.retromp3recorder.app.screens.home.components.speedbar

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedEnabledUC
import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedSetUC
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SpeedBarInteractor @Inject constructor(
    private val speedBarVisibilityMapper: SpeedBarVisibilityMapper,
    private val playbackSpeedSetUC: PlaybackSpeedSetUC,
    private val setSpeedEnabled: PlaybackSpeedEnabledUC,
    private val playerControlsRepo: PlayerControlsRepo,
    dispatcher: CoroutineDispatcher,
) : Interactor<SpeedBarContract.Input, SpeedBarContract.Output>(dispatcher) {


    override fun listRepos(): List<Flow<SpeedBarContract.Output>> {
        return listOf(
            playerControlsRepo.flow()
                .map { SpeedBarContract.Output.SpeedDataUpdate(it.speedSettings) },
            speedBarVisibilityMapper.flow().map { SpeedBarContract.Output.Visibility(it) }
        )
    }

    override suspend fun launchUseCase(input: SpeedBarContract.Input) {
        when (input) {
            is SpeedBarContract.Input.SpeedSet -> {
                playbackSpeedSetUC.execute(input.speed)
            }
            is SpeedBarContract.Input.Enable -> {
                setSpeedEnabled.execute()
            }
        }
    }
}
