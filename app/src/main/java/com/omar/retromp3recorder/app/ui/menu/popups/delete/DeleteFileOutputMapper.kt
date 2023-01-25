package com.omar.retromp3recorder.app.ui.menu.popups.delete

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object DeleteFileOutputMapper {
    fun mapOutputToState(): ObservableTransformer<DeleteFileContract.Output, DeleteFileContract.State> =
        ObservableTransformer { upstream: Observable<DeleteFileContract.Output> ->
            upstream.scan(
                DeleteFileContract.State(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<DeleteFileContract.State, DeleteFileContract.Output, DeleteFileContract.State> =
        BiFunction { oldState: DeleteFileContract.State, output: DeleteFileContract.Output ->
            when (output) {
                is DeleteFileContract.Output.ShouldDismiss ->
                    oldState.copy(shouldDismiss = output.shouldDismiss)
                is DeleteFileContract.Output.CurrentFile -> {
                    oldState.copy(fileWrapper = output.fileWrapper)
                }
            }
        }
}