package com.omar.retromp3recorder.app.screens.home.components.rangebar

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.actions.UpdatePlayerRangeUC
import com.omar.retromp3recorder.bl.settings.ActivateRangeUC
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class RangeBarInteractor @Inject constructor(
    private val rangeStateMapper: RangeBarStateMapper,
    private val updatePlayerRangeUC: UpdatePlayerRangeUC,
    private val rangeEnableRangeUC: ActivateRangeUC,
    dispatcher: CoroutineDispatcher,
) : Interactor<RangeBarView.Input, RangeBarView.State>(dispatcher) {


    override fun listRepos(): List<Flow<RangeBarView.State>> {
        return listOf(rangeStateMapper.flow())
    }

    override suspend fun FlowCollector<RangeBarView.State>.launchUseCase(input: RangeBarView.Input) {
        when (input) {
            is RangeBarView.Input.RangeSet -> {
                updatePlayerRangeUC.execute(input.range)
            }
            is RangeBarView.Input.Enable -> {
                rangeEnableRangeUC.execute()
            }
        }
    }
}
