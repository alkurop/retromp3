package com.omar.retromp3recorder.app.screens.settings.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.settings.SettingsContract
import com.omar.retromp3recorder.app.screens.settings.SettingsViewOutputMapper.mapOutputToStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModelFlow @Inject constructor(
    private val interactor: SettingsFlowInteractor
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsContract.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<SettingsContract.Input>()

    init {
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapOutputToStateFlow()
                .distinctUntilChanged()
                .collect { _state.value = it }

        }
    }

    fun onEvent(event: SettingsContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
