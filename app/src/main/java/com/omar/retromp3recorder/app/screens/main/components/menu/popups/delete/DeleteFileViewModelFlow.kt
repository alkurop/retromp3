package com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete.DeleteFileOutputMapper.mapToState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteFileViewModelFlow : ViewModel() {

    val state: StateFlow<DeleteFileContract.State>

    private val inputFlow = MutableSharedFlow<DeleteFileContract.Input>()

    @Inject
    lateinit var interactor: DeleteFileInteractorFlow

    init {
        App.appComponent.getComponent().inject(this)
        state = interactor.processIO(inputFlow)
            .mapToState()
            .stateIn(viewModelScope, Lazily, DeleteFileContract.State())
    }

    fun emit(event: DeleteFileContract.Input) {
        viewModelScope.launch { inputFlow.emit(event) }
    }
}
