package com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.app.screens.home.components.menu.MenuContract
import com.omar.retromp3recorder.bl.enablers.EnablersSwitcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class MenuInteractorFlow @Inject constructor(
    private val menuStateExcavator: MenuStateExcavatorFlow,
    private val enablersSwitcher: EnablersSwitcher,
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

