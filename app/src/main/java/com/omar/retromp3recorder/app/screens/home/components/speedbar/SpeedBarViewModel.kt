package com.omar.retromp3recorder.app.screens.home.components.speedbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpeedBarViewModel @Inject constructor(
    interactor: SpeedBarInteractor
) : ViewModel() {
    val state: StateFlow<SpeedBarContract.State>
    private val inputFlow = MutableSharedFlow<SpeedBarContract.Input>()

    init {
        state = interactor.processIO(inputFlow)
            .stateInViewModel(this, SpeedBarContract.State.Hidden)
    }

    fun onEvent(event: SpeedBarContract.Input) {
        viewModelScope.launch {
            inputFlow.emit(event)
        }
    }
}
