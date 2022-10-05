package com.omar.retromp3recorder.app.ui.menu.popups.crop

import com.omar.retromp3recorder.dto.NewNameSuggestion
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

class CropContract {
    sealed class Input {
        data class CheckCanCrop(val nameSuggestion: NewNameSuggestion) : Input()
        data class CropInPlace(val nameSuggestion: NewNameSuggestion) : Input()
        data class CropOutside(val nameSuggestion: NewNameSuggestion) : Input()
        object DismissPopup : Input()
    }

    sealed class Output {
        data class IsVisible(val isVisible: Boolean) : Output()
        data class IsActionEnabled(val isEnabled: Boolean) : Output()
        data class FileNameUpdate(val nameSuggestion: NewNameSuggestion) : Output()
    }

    data class State(
        val isOkEnabled: Boolean = false,
        val isVisible: Boolean = false,
        val nameSuggestion: NewNameSuggestion = NewNameSuggestion()
    )
}

object CropMapper {
    fun mapState(): ObservableTransformer<CropContract.Output, CropContract.State> =
        ObservableTransformer { upstream: Observable<CropContract.Output> ->
            upstream.scan(CropContract.State(), getMapper())
        }

    private fun getMapper(): BiFunction<CropContract.State, CropContract.Output, CropContract.State> =
        BiFunction { oldState, output ->
            when (output) {
                is CropContract.Output.IsVisible -> oldState.copy(isVisible = output.isVisible)
                is CropContract.Output.IsActionEnabled -> oldState.copy(isOkEnabled = output.isEnabled)
                is CropContract.Output.FileNameUpdate -> oldState.copy(nameSuggestion = output.nameSuggestion)
            }
        }
}

