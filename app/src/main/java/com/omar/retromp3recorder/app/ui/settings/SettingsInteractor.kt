package com.omar.retromp3recorder.app.ui.settings

import com.omar.retromp3recorder.bl.settings.FeatureMapSaveUC
import com.omar.retromp3recorder.storage.repo.FeatureFlagRepo
import com.omar.retromp3recorder.utils.processIO
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class SettingsInteractor @Inject constructor(
    private val featureMapSaveUC: FeatureMapSaveUC,
    private val featureFlagRepo: FeatureFlagRepo,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<SettingsView.Input, SettingsView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> (Observable<SettingsView.Output>) = {
        Observable.merge(
            listOf(
                featureFlagRepo.observe()
                    .takeOne()
                    .toObservable()
                    .map { featureFlagsCollection ->
                        SettingsView.Output.FlagsCollectionUpdate(
                            featureFlagsCollection
                        )
                    },
            )
        )
    }

    private val mapInputToUsecase: (Observable<SettingsView.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(SettingsView.Input.FlagSettingChanged::class.java)
                        .flatMapCompletable { featureMapSaveUC.execute(it.flag, it.setting) }
                )
            )
        }
}