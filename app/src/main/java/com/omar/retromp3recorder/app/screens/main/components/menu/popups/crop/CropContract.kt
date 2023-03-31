package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import com.omar.retromp3recorder.domain.NewNameSuggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

class CropContract {
    sealed class Input {
        data class CheckCanCrop(val nameSuggestion: NewNameSuggestion) : Input()
        data class CropInPlace(val nameSuggestion: NewNameSuggestion) : Input()
        data class CropOutside(val nameSuggestion: NewNameSuggestion) : Input()
    }

    sealed class Output {
        object Dismiss : Output()
        object Loading : Output()
        data class IsActionEnabled(val isEnabled: Boolean) : Output()
        data class FileNameUpdate(val nameSuggestion: NewNameSuggestion) : Output()
    }

    data class State(
        val isOkEnabled: Boolean = false,
        val isLoading: Boolean = false,
        val nameSuggestion: NewNameSuggestion = NewNameSuggestion(),
        val dismiss: Boolean = false
    )
}

object CropMapper {
    fun Flow<CropContract.Output>.mapToState(): Flow<CropContract.State> {
        return this.scan(CropContract.State()) { oldState, output ->
            when (output) {
                is CropContract.Output.IsActionEnabled -> oldState.copy(isOkEnabled = output.isEnabled)
                is CropContract.Output.Dismiss -> oldState.copy(dismiss = true)
                is CropContract.Output.Loading -> oldState.copy(isLoading = true, isOkEnabled = false)
                is CropContract.Output.FileNameUpdate -> oldState.copy(nameSuggestion = output.nameSuggestion)
            }
        }
    }
}

