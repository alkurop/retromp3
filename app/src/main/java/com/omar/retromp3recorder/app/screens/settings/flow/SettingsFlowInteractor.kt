package com.omar.retromp3recorder.app.screens.settings.flow

import com.omar.retromp3recorder.app.screens.settings.SettingsContract
import com.omar.retromp3recorder.bl.settings.FeatureMapSaveUC
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SettingsFlowInteractor @Inject constructor(
    private val featureMapSaveUC: FeatureMapSaveUC,
    private val featureFlagRepo: FeatureFlagRepo,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun processIO(upstream: Flow<SettingsContract.Input>): Flow<SettingsContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge()
    }

    private fun Flow<SettingsContract.Input>.processInputs(): Flow<SettingsContract.Output> {
        return this.transform { event ->
            when (event) {
                is SettingsContract.Input.FlagSettingChanged ->
                    launch {
                        featureMapSaveUC.execute(event.flag, event.setting)
                    }
            }
        }
    }

    private fun listenToRepos(): Flow<SettingsContract.Output> {
        return listOf<Flow<SettingsContract.Output>>(
            featureFlagRepo.observeFlow()
                .map { featureFlagsCollection ->
                    SettingsContract.Output.FlagsCollectionUpdate(
                        featureFlagsCollection
                    )
                }
        ).merge()
    }
}
