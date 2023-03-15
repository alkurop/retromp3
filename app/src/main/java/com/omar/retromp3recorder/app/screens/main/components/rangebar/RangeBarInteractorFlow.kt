package com.omar.retromp3recorder.app.screens.main.components.rangebar

import com.omar.retromp3recorder.bl.audio.UpdatePlayerRangeUC
import com.omar.retromp3recorder.bl.settings.ActivateRangeUC
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RangeBarInteractorFlow @Inject constructor(
    private val rangeStateMapper: RangeBarStateMapperFlow,
    private val updatePlayerRangeUC: UpdatePlayerRangeUC,
    private val rangeEnableRangeUC: ActivateRangeUC,
    private val dispatcher: CoroutineDispatcher,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(upstream: Flow<RangeBarView.Input>): Flow<RangeBarView.State> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<RangeBarView.Input>.processInputs(): Flow<RangeBarView.State> {
        return this.transform { input ->
            launch {
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
    }


    private fun listenToRepos(): Flow<RangeBarView.State> {
        return rangeStateMapper.flow()
    }
}
