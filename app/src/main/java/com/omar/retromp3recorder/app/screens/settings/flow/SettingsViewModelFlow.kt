package com.omar.retromp3recorder.app.screens.settings.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.settings.SettingsContract
import com.omar.retromp3recorder.app.screens.settings.SettingsViewOutputMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModelFlow @Inject constructor(
    interactor: SettingsFlowInteractor
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<SettingsContract.Input>()

    val state = interactor.processIO(viewModelScope, inputFlow)        .mapOutputToStateFlow()
        .distinctUntilChanged()
        .stateInViewModel(this, SettingsContract.State())


    fun onEvent(event: SettingsContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
