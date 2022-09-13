package com.omar.retromp3recorder.app.ui.files.selector

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object SelectorOutputMapper {
    fun mapOutputToState(): ObservableTransformer<SelectorView.Output, SelectorView.State> =
        ObservableTransformer { upstream: Observable<SelectorView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<SelectorView.State, SelectorView.Output, SelectorView.State> =
        BiFunction { oldState: SelectorView.State, output: SelectorView.Output ->
            when (output) {
                is SelectorView.Output.FileList -> {
                    oldState.copy(items = output.items)
                }
                is SelectorView.Output.CurrentFile -> {
                    val selectedFile = output.filePath
                    oldState.copy(
                        selectedFile = selectedFile
                    )
                }
            }
        }

    private fun getDefaultViewModel() = SelectorView.State(
        items = null,
        // selected file has to be here,   in case selection comes before list
        selectedFile = null
    )
}