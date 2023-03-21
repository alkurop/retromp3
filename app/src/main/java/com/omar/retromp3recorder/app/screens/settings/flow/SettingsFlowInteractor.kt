package com.omar.retromp3recorder.app.screens.settings.flow

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.app.screens.settings.SettingsContract
import com.omar.retromp3recorder.bl.settings.FeatureMapSaveUC
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsFlowInteractor @Inject constructor(
    private val featureMapSaveUC: FeatureMapSaveUC,
    private val featureFlagRepo: FeatureFlagRepo,
    dispatcher: CoroutineDispatcher
) : Interactor<SettingsContract.Input, SettingsContract.Output>(dispatcher) {
    override fun listRepos(): List<Flow<SettingsContract.Output>> {
        return listOf(
            featureFlagRepo.flow()
                .map { featureFlagsCollection ->
                    SettingsContract.Output.FlagsCollectionUpdate(
                        featureFlagsCollection
                    )
                }
        )
    }

    override suspend fun FlowCollector<SettingsContract.Output>.launchUseCase(input: SettingsContract.Input) {
        when (input) {
            is SettingsContract.Input.FlagSettingChanged ->
                featureMapSaveUC.execute(input.flag, input.setting)
        }
    }
}
