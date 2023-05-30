package com.omar.retromp3recorder.app.screens.home.components.joined_progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.screens.home.components.joined_progress.JoinedProgressViewMapper.mapOutputToStateFlow
import com.omar.retromp3recorder.app.utils.stateInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinedProgressViewModel @Inject constructor(
    interactor: JoinedProgressInteractor
) : ViewModel() {

    private val inputFlow = MutableSharedFlow<JoinedProgressContract.In>()

    val state = interactor.processIO(inputFlow)
        .mapOutputToStateFlow()
        .stateInViewModel(this, JoinedProgressContract.State())

    fun onEvent(event: JoinedProgressContract.In) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
