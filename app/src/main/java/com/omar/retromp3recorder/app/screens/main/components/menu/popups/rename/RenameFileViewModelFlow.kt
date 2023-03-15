package com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename.RenameFileOutputMapper.mapToState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class RenameFileViewModelFlow : ViewModel() {

    val state: StateFlow<RenameFileContract.State>
    private val inputFlow = MutableSharedFlow<RenameFileContract.Input>()


    @Inject
    lateinit var interactor: RenameFileInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        state = interactor.processIO(inputFlow)
            .mapToState()
            .stateIn(viewModelScope, SharingStarted.Lazily, RenameFileContract.State())
    }

    fun onEvent(event: RenameFileContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
