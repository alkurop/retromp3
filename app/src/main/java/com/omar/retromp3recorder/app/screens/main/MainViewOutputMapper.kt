package com.omar.retromp3recorder.app.screens.main

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.domain.FeatureFlag
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object MainViewOutputMapper {
    fun mapOutputToState(): ObservableTransformer<MainViewContract.Output, MainViewContract.State> =
        ObservableTransformer { upstream: Observable<MainViewContract.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<MainViewContract.State, MainViewContract.Output, MainViewContract.State> =
        BiFunction { oldState: MainViewContract.State, output: MainViewContract.Output ->
            when (output) {
                is MainViewContract.Output.RequestPermissionsOutput -> oldState.copy(
                    requestForPermissions = Shell(output.permissionsToRequest)
                )
                is MainViewContract.Output.RequestScreenCapture ->
                    oldState.copy(
                        requestForScreenCapture = Shell(output.shouldRequest)
                    )
                is MainViewContract.Output.SettingsUpdated -> {
                    val isLogViewEnabled =
                        output.featureFlagsCollection.isEnabled(FeatureFlag.LogView)
                    val shouldKeepScreenOn =
                        output.featureFlagsCollection.isEnabled(FeatureFlag.KeepScreenOn)
                    oldState.copy(
                        isLogViewEnabled = isLogViewEnabled,
                        shouldKeepScreenOn = shouldKeepScreenOn
                    )
                }
            }
        }

    private fun getDefaultViewModel() = MainViewContract.State(
        requestForPermissions = Shell.empty(),
        requestForScreenCapture = Shell.empty(),
    )
}
