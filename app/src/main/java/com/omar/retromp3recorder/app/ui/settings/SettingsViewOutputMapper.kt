package com.omar.retromp3recorder.app.ui.settings

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object SettingsViewOutputMapper {
    fun mapOutputToState(): ObservableTransformer<SettingsView.Output, SettingsView.State> =
        ObservableTransformer { upstream: Observable<SettingsView.Output> ->
            upstream.scan(
                SettingsView.State(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<SettingsView.State, SettingsView.Output, SettingsView.State> =
        BiFunction { oldState: SettingsView.State, output: SettingsView.Output ->
            when (output) {
                is SettingsView.Output.FlagsCollectionUpdate -> oldState.copy(
                    featureFlagsCollection = output.featureFlagsCollection
                )
            }
        }
}
