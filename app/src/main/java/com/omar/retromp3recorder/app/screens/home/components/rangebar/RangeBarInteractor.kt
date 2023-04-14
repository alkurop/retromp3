package com.omar.retromp3recorder.app.screens.home.components.rangebar

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.actions.UpdatePlayerRangeUC
import com.omar.retromp3recorder.bl.settings.ActivateRangeUC
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RangeBarInteractor @Inject constructor(
    private val rangeStateMapper: RangeBarContentMapper,
    private val updatePlayerRangeUC: UpdatePlayerRangeUC,
    private val rangeEnableRangeUC: ActivateRangeUC,
    private val visibilityMapper: RangeBarVisibilityMapper,
    dispatcher: CoroutineDispatcher,
) : Interactor<RangeBarContract.Input, RangeBarContract.Output>(dispatcher) {

    override fun listRepos(): List<Flow<RangeBarContract.Output>> {
        return listOf(
            rangeStateMapper.flow().map { RangeBarContract.Output.BarContentUpdate(it) },
            visibilityMapper.flow().map { RangeBarContract.Output.Visibility(it) }
        )
    }

    override suspend fun launchUseCase(input: RangeBarContract.Input) {
        when (input) {
            is RangeBarContract.Input.RangeSet -> {
                updatePlayerRangeUC.execute(input.range)
            }
            is RangeBarContract.Input.Enable -> {
                rangeEnableRangeUC.execute()
            }
        }
    }
}
