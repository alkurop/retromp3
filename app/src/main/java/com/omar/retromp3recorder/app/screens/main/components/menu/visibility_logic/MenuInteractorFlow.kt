package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.bl.enablers.EnablersSwitcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@OptIn(FlowPreview::class)
class MenuInteractorFlow @Inject constructor(
    private val menuStateExcavator: MenuStateExcavatorFlow,
    private val enablersSwitcher: EnablersSwitcher,
    private val dispatcher: CoroutineDispatcher,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(upstream: Flow<MenuContract.Input>): Flow<MenuContract.State> {
        return listOf(
            upstream.processInputs(),
            menuStateExcavator.flow()
        ).merge().flowOn(dispatcher).distinctUntilChanged()
    }


    private fun Flow<MenuContract.Input>.processInputs(): Flow<MenuContract.State> {
        return this.flatMapMerge { input ->
            flow {
                when (input) {
                    is MenuContract.Input.Enable -> {
                        enablersSwitcher.execute(input.enabler, input.isEnabled).blockingAwait()
                    }
                }
            }
        }
    }
}

