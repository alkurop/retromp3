package com.omar.retromp3recorder.app.screens.home.components.rangebar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RangeBarViewModelFlow @Inject constructor(
    interactor: RangeBarInteractorFlow
) : ViewModel() {
    val state: StateFlow<RangeBarView.State>
    private val inputFlow = MutableSharedFlow<RangeBarView.Input>()

    init {
        state = interactor.processIO(inputFlow)
            .stateInViewModel(this, RangeBarView.State.Hidden)
    }

    fun onEvent(event: RangeBarView.Input) {
        viewModelScope.launch {
            inputFlow.emit(event)
        }
    }
}
