package com.omar.retromp3recorder.app.ui.menu.popups.rename

import com.omar.retromp3recorder.domain.ExistingFileWrapper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object RenameFileContract {
    data class State(
        val newName: String? = null,
        val isOkButtonEnabled: Boolean = false,
        val fileWrapper: ExistingFileWrapper? = null
    )

    sealed class Input {
        data class CheckCanRename(val newName: String) : Input()
        data class Rename(val newName: String) : Input()
        object DismissPopup : Input()
    }

    sealed class Output {
        data class OkButtonState(val isEnabled: Boolean, val newName: String?) : Output()
        data class CurrentFile(val fileWrapper: ExistingFileWrapper?) : Output()
    }
}

object RenameFileOutputMapper {
    fun mapOutputToState(): ObservableTransformer<RenameFileContract.Output, RenameFileContract.State> =
        ObservableTransformer { upstream: Observable<RenameFileContract.Output> ->
            upstream.scan(
                RenameFileContract.State(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<RenameFileContract.State, RenameFileContract.Output, RenameFileContract.State> =
        BiFunction { oldState: RenameFileContract.State, output: RenameFileContract.Output ->
            when (output) {
                is RenameFileContract.Output.OkButtonState -> oldState.copy(
                    isOkButtonEnabled = output.isEnabled,
                    newName = output.newName
                )
                is RenameFileContract.Output.CurrentFile -> oldState.copy(fileWrapper = output.fileWrapper)
            }
        }
}
