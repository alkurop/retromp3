package com.omar.retromp3recorder.app.screens.home.components.speedbar

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedEnabledUC
import com.omar.retromp3recorder.bl.audio.effects.PlaybackSpeedSetUC
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class SpeedBarInteractor @Inject constructor(
    private val speedBarStateMapper: SpeedBarStateMapper,
    private val playbackSpeedSetUC: PlaybackSpeedSetUC,
    private val setSpeedEnabled: PlaybackSpeedEnabledUC,
    dispatcher: CoroutineDispatcher,
) : Interactor<SpeedBarContract.Input, SpeedBarContract.State>(dispatcher) {


    override fun listRepos(): List<Flow<SpeedBarContract.State>> {
        return listOf(speedBarStateMapper.flow())
    }

    override suspend fun FlowCollector<SpeedBarContract.State>.launchUseCase(input: SpeedBarContract.Input) {
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
