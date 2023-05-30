package com.omar.retromp3recorder.app.screens.home.components.language

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
class LanguageViewModel @Inject constructor(
    interactor: LanguageInteractor
) : ViewModel() {
    private val inputFlow = MutableSharedFlow<LanguageContract.Input>()

    val state = interactor.processIO(inputFlow)
        .mapOutputToStateFlow()
        .stateInViewModel(this, LanguageContract.State())

    fun onEvent(event: LanguageContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}


private fun Flow<LanguageContract.Output>.mapOutputToStateFlow(): Flow<LanguageContract.State> {
    return this.scan(LanguageContract.State()) { oldState, output ->
        when (output) {
            is LanguageContract.Output.Visibility -> oldState.copy(isVisible = output.isVisible)
        }
    }
}
