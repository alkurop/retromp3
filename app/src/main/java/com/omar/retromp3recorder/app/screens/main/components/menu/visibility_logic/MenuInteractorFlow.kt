package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.bl.enablers.EnablersSwitcherSuspend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class MenuInteractorFlow @Inject constructor(
    private val menuStateExcavator: MenuStateExcavatorFlow,
    private val enablersSwitcher: EnablersSwitcherSuspend,
    dispatcher: CoroutineDispatcher,
) : Interactor<MenuContract.Input, MenuContract.State>(dispatcher) {

    override fun listRepos(): List<Flow<MenuContract.State>> {
        return listOf(menuStateExcavator.flow())
    }

    override suspend fun FlowCollector<MenuContract.State>.launchUseCase(input: MenuContract.Input) {
        when (input) {
            is MenuContract.Input.Enable -> {
                enablersSwitcher.execute(input.enabler, input.isEnabled)
            }
        }
    }
}

