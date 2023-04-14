package com.omar.retromp3recorder.app.screens.home

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.audio.record.UpdateMediaProjectionUC
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.global.MediaProjectionStateRepo
import com.omar.retromp3recorder.utils.platform.shellUnwrap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeViewInteractor @Inject constructor(
    private val mediaProjectionRequestBus: MediaProjectionStateRepo,
    private val updateMediaProjectionUC: UpdateMediaProjectionUC,
    private val featureFlagRepo: FeatureFlagRepo,
    dispatcher: CoroutineDispatcher
) : Interactor<HomeViewContract.Input, HomeViewContract.Output>(dispatcher) {

    override fun listRepos(): List<Flow<HomeViewContract.Output>> {
        return listOf(
            featureFlagRepo.flow()
                .map { features -> HomeViewContract.Output.SettingsUpdated(features) },
            mediaProjectionRequestBus.flow().map {
                it.request
            }.shellUnwrap()
                .filterNotNull()
                .map { request ->
                    HomeViewContract.Output.RequestScreenCapture(Shell(request))
                })
    }

    override suspend fun launchUseCase(input: HomeViewContract.Input) {
        when (input) {
            is HomeViewContract.Input.MediaProjectionUpdated -> {
                updateMediaProjectionUC.execute(input.mediaProjection)
            }
        }
    }
}
