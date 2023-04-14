package com.omar.retromp3recorder.app.screens.home.components.rangebar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RangeBarViewModel @Inject constructor(
    interactor: RangeBarInteractor
) : ViewModel() {
    private val inputFlow = MutableSharedFlow<RangeBarContract.Input>()

    val state = interactor
        .processIO(viewModelScope, inputFlow)
        .mapToState()
        .stateInViewModel(this, RangeBarContract.State())

    fun onEvent(event: RangeBarContract.Input) {
        viewModelScope.launch {
            inputFlow.emit(event)
        }
    }
}

private fun Flow<RangeBarContract.Output>.mapToState(): Flow<RangeBarContract.State> {
    return this.scan(RangeBarContract.State()) { oldState, output ->
        when (output) {
            is RangeBarContract.Output.Visibility -> oldState.copy(isVisible = output.isVisible)
            is RangeBarContract.Output.BarContentUpdate -> oldState.copy(barContent = output.barContent)
        }
    }
}
