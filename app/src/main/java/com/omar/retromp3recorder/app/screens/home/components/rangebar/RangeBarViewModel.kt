package com.omar.retromp3recorder.app.screens.home.components.rangebar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RangeBarViewModel @Inject constructor(
    interactor: RangeBarInteractor
) : ViewModel() {
    private val inputFlow = MutableSharedFlow<RangeBarView.Input>()

    val state = interactor.processIO(viewModelScope, inputFlow)
        .stateInViewModel(this, RangeBarView.State.Hidden)

    fun onEvent(event: RangeBarView.Input) {
        viewModelScope.launch {
            inputFlow.emit(event)
        }
    }
}
