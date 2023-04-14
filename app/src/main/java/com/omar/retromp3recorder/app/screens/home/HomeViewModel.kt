package com.omar.retromp3recorder.app.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import com.omar.retromp3recorder.domain.FeatureFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    interactor: HomeViewInteractor
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<HomeViewContract.Input>()

    val state = interactor.processIO(viewModelScope, inputFlow)
        .mapToState()
        .stateInViewModel(this, HomeViewContract.State())

    fun emit(event: HomeViewContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}

private fun Flow<HomeViewContract.Output>.mapToState(): Flow<HomeViewContract.State> {
    return this.scan(HomeViewContract.State()) { oldState, output ->
        when (output) {
            is HomeViewContract.Output.RequestScreenCapture ->
                oldState.copy(
                    requestForScreenCapture = output.shouldRequest
                )
            is HomeViewContract.Output.SettingsUpdated -> {
                val isLogViewEnabled =
                    output.featureFlagsCollection.isEnabled(FeatureFlag.LogView)
                oldState.copy(
                    isLogViewEnabled = isLogViewEnabled,
                )
            }
        }
    }
}
