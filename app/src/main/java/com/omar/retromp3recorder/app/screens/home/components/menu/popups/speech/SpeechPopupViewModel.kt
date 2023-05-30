package com.omar.retromp3recorder.app.screens.home.components.menu.popups.speech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpeechPopupViewModel @Inject constructor(
    interactor: SpeechPopupInteractor
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<SpeechPopupContract.Input>()

    val state = interactor.processIO(inputFlow)
        .stateInViewModel(this, SpeechPopupContract.State())

    fun emit(event: SpeechPopupContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
