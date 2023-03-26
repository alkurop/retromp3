package com.omar.retromp3recorder.app.screens.main

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.record.UpdateMediaProjectionUCSuspend
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.platform.shellUnwrap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainViewInteractor @Inject constructor(
    private val mediaProjectionRequestBus: MediaProjectionStateRepo,
    private val updateMediaProjectionUC: UpdateMediaProjectionUCSuspend,
    private val featureFlagRepo: FeatureFlagRepo,
    dispatcher: CoroutineDispatcher
) : Interactor<MainViewContract.Input, MainViewContract.Output>(dispatcher) {

    override fun listRepos(): List<Flow<MainViewContract.Output>> {
        return listOf(featureFlagRepo.flow()
            .map { features -> MainViewContract.Output.SettingsUpdated(features) },
            mediaProjectionRequestBus.flow().map { it.request }.shellUnwrap()
                .map { request -> MainViewContract.Output.RequestScreenCapture(request) })
    }

    override suspend fun FlowCollector<MainViewContract.Output>.launchUseCase(input: MainViewContract.Input) {
        when (input) {
            is MainViewContract.Input.MediaProjectionUpdated -> {
                updateMediaProjectionUC.execute(input.mediaProjection)
            }
        }
    }
}
