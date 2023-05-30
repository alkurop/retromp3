package com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.home.components.menu.MenuContract
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    interactor: MenuInteractor
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<MenuContract.Input>()

    val state = interactor.processIO(inputFlow)
        .stateInViewModel(this, MenuContract.State())

    fun onEvent(event: MenuContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
