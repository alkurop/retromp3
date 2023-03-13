package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.bl.enablers.EnablersSwitcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MenuInteractorFlow @Inject constructor(
    private val menuStateExcavator: MenuStateExcavator,
    private val eneblersSwitcher: EnablersSwitcher,
    dispatcher: CoroutineDispatcher,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(upstream: Flow<MenuContract.Input>): Flow<MenuContract.State> {
        return listOf(
            upstream.processInputs(),
            menuStateExcavator.observe().asFlow()
        ).merge().distinctUntilChanged()
    }


    private fun Flow<MenuContract.Input>.processInputs(): Flow<MenuContract.State> {
        return this.transform { input ->
            launch {
                when (input) {
                    is MenuContract.Input.Enable -> {
                        eneblersSwitcher.execute(input.enabler, input.isEnabled).blockingAwait()
                    }
                }
            }
        }
    }
}

