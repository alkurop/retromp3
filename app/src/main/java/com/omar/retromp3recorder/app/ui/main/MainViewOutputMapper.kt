package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FeatureFlag
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object MainViewOutputMapper {
    fun mapOutputToState(): ObservableTransformer<MainView.Output, MainView.State> =
        ObservableTransformer { upstream: Observable<MainView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<MainView.State, MainView.Output, MainView.State> =
        BiFunction { oldState: MainView.State, output: MainView.Output ->
            when (output) {
                is MainView.Output.RequestPermissionsOutput -> oldState.copy(
                    requestForPermissions = Shell(output.permissionsToRequest)
                )
                is MainView.Output.RequestScreenCapture ->
                    oldState.copy(
                        requestForScreenCapture = Shell(output.shouldRequest)
                    )
                is MainView.Output.SettingsUpdated -> {
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

    private fun getDefaultViewModel() = MainView.State(
        requestForPermissions = Shell.empty(),
        requestForScreenCapture = Shell.empty(),
    )
}
