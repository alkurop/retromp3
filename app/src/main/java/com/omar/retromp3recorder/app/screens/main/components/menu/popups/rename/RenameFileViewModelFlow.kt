package com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename.RenameFileOutputMapper.mapToState
import com.omar.retromp3recorder.app.utils.stateInViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
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
            .stateInViewModel(this, RenameFileContract.State())
    }

    fun onEvent(event: RenameFileContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
