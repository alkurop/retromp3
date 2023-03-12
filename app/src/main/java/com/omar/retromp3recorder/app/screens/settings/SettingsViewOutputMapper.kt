package com.omar.retromp3recorder.app.screens.settings

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object SettingsViewOutputMapper {
    fun mapOutputToState(): ObservableTransformer<SettingsContract.Output, SettingsContract.State> =
        ObservableTransformer { upstream: Observable<SettingsContract.Output> ->
            upstream.scan(
                SettingsContract.State(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<SettingsContract.State, SettingsContract.Output, SettingsContract.State> =
        BiFunction { oldState: SettingsContract.State, output: SettingsContract.Output ->
            when (output) {
                is SettingsContract.Output.FlagsCollectionUpdate -> oldState.copy(
                    featureFlagsCollection = output.featureFlagsCollection
                )
            }
        }
}
