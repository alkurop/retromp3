package com.omar.retromp3recorder.app.screens.main

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.app.FlowViewModel
import com.omar.retromp3recorder.bl.audio.UpdateMediaProjectionUC
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.domain.shellUnwrap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mediaProjectionRequestBus: MediaProjectionStateRepo,
    private val updateMediaProjectionUC: UpdateMediaProjectionUC,
    private val featureFlagRepo: FeatureFlagRepo,
    val toastRepo: ToastRepo,
    dispatcher: CoroutineDispatcher

) : FlowViewModel<MainViewContract.Input, MainViewContract.Output, MainViewContract.State>(
    dispatcher
) {
    override val defaultState: MainViewContract.State = MainViewContract.State()
    override fun listenToRepos(): List<Flow<MainViewContract.Output>> {
        return listOf(
            featureFlagRepo.flow()
                .map { features -> MainViewContract.Output.SettingsUpdated(features) },
            mediaProjectionRequestBus.flow()
                .map { it.request }
                .shellUnwrap()
                .map { request -> MainViewContract.Output.RequestScreenCapture(request) }
        )
    }

    override suspend fun FlowCollector<MainViewContract.Output>.getUsecase(event: MainViewContract.Input) {
        when (event) {
            is MainViewContract.Input.MediaProjectionUpdated -> {
                updateMediaProjectionUC.execute(event.mediaProjection).blockingAwait()
            }
        }
    }

    override fun Flow<MainViewContract.Output>.mapToState(): Flow<MainViewContract.State> {
        return this.scan(defaultState) { oldState, output ->
            when (output) {
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
    }
}
