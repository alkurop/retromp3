package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModelFlow @Inject constructor(
    interactor: MenuInteractorFlow
) : ViewModel() {

    val state: StateFlow<MenuContract.State>
    private val inputFlow = MutableSharedFlow<MenuContract.Input>()

    init {
        state = interactor.processIO(inputFlow)
            .stateInViewModel(this, MenuContract.State())
    }

    fun onEvent(event: MenuContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
