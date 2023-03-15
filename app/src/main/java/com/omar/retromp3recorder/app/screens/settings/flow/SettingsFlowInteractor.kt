package com.omar.retromp3recorder.app.screens.settings.flow

import com.omar.retromp3recorder.app.screens.settings.SettingsContract
import com.omar.retromp3recorder.bl.settings.FeatureMapSaveUC
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SettingsFlowInteractor @Inject constructor(
    private val featureMapSaveUC: FeatureMapSaveUC,
    private val featureFlagRepo: FeatureFlagRepo,
    private val dispatcher: CoroutineDispatcher
) {
    fun processIO(upstream: Flow<SettingsContract.Input>): Flow<SettingsContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<SettingsContract.Input>.processInputs(): Flow<SettingsContract.Output> {
        return this.transform { event ->
            when (event) {
                is SettingsContract.Input.FlagSettingChanged ->
                    featureMapSaveUC.execute(event.flag, event.setting)
            }
        }
    }

    private fun listenToRepos(): Flow<SettingsContract.Output> {
        return listOf<Flow<SettingsContract.Output>>(
            featureFlagRepo.flow()
                .map { featureFlagsCollection ->
                    SettingsContract.Output.FlagsCollectionUpdate(
                        featureFlagsCollection
                    )
                }
        ).merge()
    }
}
