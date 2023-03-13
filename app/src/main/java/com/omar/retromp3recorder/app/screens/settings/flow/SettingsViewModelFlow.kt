package com.omar.retromp3recorder.app.screens.settings.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.settings.SettingsContract
import com.omar.retromp3recorder.app.screens.settings.SettingsViewOutputMapper.mapOutputToStateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModelFlow : ViewModel() {
    private val _state = MutableStateFlow(SettingsContract.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<SettingsContract.Input>()

    @Inject
    lateinit var interactor: SettingsFlowInteractor

    init {
        App.appComponent.inject(this)
        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapOutputToStateFlow()
                .collect { _state.value = it }

        }
    }

    fun onEvent(event: SettingsContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
