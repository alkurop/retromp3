package com.omar.retromp3recorder.app.screens.home.components.speedbar

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
class SpeedBarViewModel @Inject constructor(
    interactor: SpeedBarInteractor
) : ViewModel() {
    private val inputFlow = MutableSharedFlow<SpeedBarContract.Input>()

    val state = interactor.processIO()
        .mapToState()
        .stateInViewModel(this, SpeedBarContract.State())

    fun onEvent(event: SpeedBarContract.Input) {
        viewModelScope.launch {
            inputFlow.emit(event)
        }
    }
}

private fun  Flow<SpeedBarContract.Output>.mapToState(): Flow<SpeedBarContract.State> {
    return this.scan(SpeedBarContract.State()) { oldState, output ->
        when (output) {
            is SpeedBarContract.Output.Visibility -> oldState.copy(isVisible = output.isVisible)
            is SpeedBarContract.Output.SpeedDataUpdate -> oldState.copy(speedData = output.speed)
        }
    }
}
