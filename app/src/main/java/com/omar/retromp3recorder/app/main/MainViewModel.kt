package com.omar.retromp3recorder.app.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.utils.domain.toShell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: MainActivityInteractor
) : ViewModel() {
    private val _state = MutableStateFlow(MainActivityContract.State())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            interactor.processIO()
                .mapToState()
                .collect { _state.value = it }
        }
    }
}


private fun Flow<MainActivityContract.Output>.mapToState(): Flow<MainActivityContract.State> {
    return this.scan(MainActivityContract.State()) { oldState, output ->
        when (output) {
            is MainActivityContract.Output.ShowToast -> {
                oldState.copy(toast = output.text.toShell())
            }
            is MainActivityContract.Output.SettingsUpdated -> {
                val shouldKeepScreenOn =
                    output.featureFlagsCollection.isEnabled(FeatureFlag.KeepScreenOn)
                oldState.copy(
                    shouldKeepScreenOn = shouldKeepScreenOn
                )
            }
        }
    }
}
