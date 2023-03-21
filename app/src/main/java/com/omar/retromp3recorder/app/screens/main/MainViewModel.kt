package com.omar.retromp3recorder.app.screens.main

import androidx.lifecycle.viewModelScope
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.app.FlowViewModel
import com.omar.retromp3recorder.bl.audio.UpdateMediaProjectionUC
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.domain.shellUnwrap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    mediaProjectionRequestBus: MediaProjectionStateRepo,
    private val updateMediaProjectionUC: UpdateMediaProjectionUC,
    featureFlagRepo: FeatureFlagRepo,
    dispatcher: CoroutineDispatcher
) : FlowViewModel<MainViewContract.Input, MainViewContract.Output, MainViewContract.State>(
    dispatcher
) {
    override val initialState: MainViewContract.State = MainViewContract.State()

    override val repos = listOf(
        featureFlagRepo.flow()
            .map { features -> MainViewContract.Output.SettingsUpdated(features) },
        mediaProjectionRequestBus.flow().map { it.request }.shellUnwrap()
            .map { request -> MainViewContract.Output.RequestScreenCapture(request) }
    )


    override val launchUsecase: suspend FlowCollector<MainViewContract.Output>.(MainViewContract.Input) -> Unit =
        {
            when (it) {
                is MainViewContract.Input.MediaProjectionUpdated -> {
                    updateMediaProjectionUC.execute(it.mediaProjection).blockingAwait()
                }
            }
        }

    override val stateMapper: (MainViewContract.State, MainViewContract.Output) -> MainViewContract.State =
        { oldState, output ->
            when (output) {
                is MainViewContract.Output.RequestScreenCapture -> oldState.copy(
                    requestForScreenCapture = Shell(output.shouldRequest)
                )
                is MainViewContract.Output.SettingsUpdated -> {
                    val isLogViewEnabled =
                        output.featureFlagsCollection.isEnabled(FeatureFlag.LogView)
                    val shouldKeepScreenOn =
                        output.featureFlagsCollection.isEnabled(FeatureFlag.KeepScreenOn)
                    oldState.copy(
                        isLogViewEnabled = isLogViewEnabled, shouldKeepScreenOn = shouldKeepScreenOn
                    )
                }
            }
        }

    init {
       launch()
    }
}
