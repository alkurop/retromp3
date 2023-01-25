package com.omar.retromp3recorder.app.ui.menu.popups.delete

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object DeleteFileOutputMapper {
    fun mapOutputToState(): ObservableTransformer<DeleteFileView.Output, DeleteFileView.State> =
        ObservableTransformer { upstream: Observable<DeleteFileView.Output> ->
            upstream.scan(
                DeleteFileView.State(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<DeleteFileView.State, DeleteFileView.Output, DeleteFileView.State> =
        BiFunction { oldState: DeleteFileView.State, output: DeleteFileView.Output ->
            when (output) {
                is DeleteFileView.Output.ShouldDismiss ->
                    oldState.copy(shouldDismiss = output.shouldDismiss)
                is DeleteFileView.Output.CurrentFile -> {
                    oldState.copy(fileWrapper = output.fileWrapper)
                }
            }
        }
}