package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModelFlow : ViewModel() {

    private val _state = MutableStateFlow(MenuContract.State())
    val state = _state.asStateFlow()
    private val inputFlow = MutableSharedFlow<MenuContract.Input>()

    @Inject
    lateinit var interactor: MenuInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .collect{_state.value = it}
        }
    }

    fun onEvent(event: MenuContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
