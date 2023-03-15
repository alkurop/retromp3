package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModelFlow : ViewModel() {

    val state: StateFlow<MenuContract.State>
    private val inputFlow = MutableSharedFlow<MenuContract.Input>()

    @Inject
    lateinit var interactor: MenuInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        state = interactor.processIO(inputFlow)
            .stateIn(viewModelScope, Lazily, MenuContract.State())
    }

    fun onEvent(event: MenuContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
