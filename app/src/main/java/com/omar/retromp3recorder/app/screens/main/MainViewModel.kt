package com.omar.retromp3recorder.app.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.domain.FeatureFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: MainViewInteractor
) :ViewModel(){
    private val _state = MutableStateFlow(MainViewContract.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<MainViewContract.Input>()

    init {
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapToState()
                .collect { _state.value = it }
        }
    }

    fun emit(event: MainViewContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}

private fun Flow<MainViewContract.Output>.mapToState(): Flow<MainViewContract.State> {
    return this.scan(MainViewContract.State()) { oldState, output ->
        when (output) {
            is MainViewContract.Output.RequestScreenCapture ->
                oldState.copy(
                    requestForScreenCapture = output.shouldRequest
                )
            is MainViewContract.Output.SettingsUpdated -> {
                val isLogViewEnabled =
                    output.featureFlagsCollection.isEnabled(FeatureFlag.LogView)
                val shouldKeepScreenOn =
                    output.featureFlagsCollection.isEnabled(FeatureFlag.KeepScreenOn)
                oldState.copy(
                    isLogViewEnabled = isLogViewEnabled,
                    shouldKeepScreenOn = shouldKeepScreenOn
                )
            }
        }
    }
}
