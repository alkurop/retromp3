package com.omar.retromp3recorder.app.main

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainActivityInteractor @Inject constructor(
    private val featureFlagRepo: FeatureFlagRepo,
    private val toastRepo: ToastRepo,
    dispatcher: CoroutineDispatcher
) : Interactor<Unit, MainActivityContract.Output>(dispatcher) {
    override suspend fun launchUseCase(input: Unit) =
        Unit

    override fun listRepos(): List<Flow<MainActivityContract.Output>> = listOf(
        toastRepo.flow().map {
            MainActivityContract.Output.ShowToast(it)
        },
        featureFlagRepo.flow()
            .map { features -> MainActivityContract.Output.SettingsUpdated(features) }
    )
}
