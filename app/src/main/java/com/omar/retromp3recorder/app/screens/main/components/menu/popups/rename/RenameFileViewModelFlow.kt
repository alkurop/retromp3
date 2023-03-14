package com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename.RenameFileOutputMapper.mapToState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RenameFileViewModelFlow : ViewModel() {

    private val _state = MutableStateFlow(RenameFileContract.State())
    val state = _state.asStateFlow()

    private val inputFlow = MutableSharedFlow<RenameFileContract.Input>()


    @Inject
    lateinit var interactor: RenameFileInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)

        viewModelScope.launch {
            interactor.processIO(inputFlow)
                .mapToState()
                .collect { _state.value = it }

        }
    }

    fun onEvent(event: RenameFileContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
