package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.ui.main.MainView.Output
import com.omar.retromp3recorder.bl.audio.UpdateMediaProjectionUC
import com.omar.retromp3recorder.bl.system.CheckAllPermissionsUC
import com.omar.retromp3recorder.storage.repo.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.MediaProjectionStateRepo
import com.omar.retromp3recorder.storage.repo.PermissionsRequestBus
import com.omar.retromp3recorder.utils.flatMapGhost
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class MainViewInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val permissionsRequestBus: PermissionsRequestBus,
    private val checkAllPermissionsUC: CheckAllPermissionsUC,
    private val mediaProjectionRequestBus: MediaProjectionStateRepo,
    private val updateMediaProjectionUC: UpdateMediaProjectionUC,
    private val featureFlagRepo: FeatureFlagRepo,
) {

    fun processIO(): ObservableTransformer<MainView.Input, Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> (Observable<Output>) = {
        Observable.merge(
            listOf(
                permissionsRequestBus.observe()
                    .ofType(PermissionsRequestBus.ShouldRequestPermissions.Denied::class.java)
                    .map { it.permissions }
                    .flatMapGhost()
                    .map { denied -> Output.RequestPermissionsOutput(denied) },
                mediaProjectionRequestBus.observe()
                    .map { it.request }
                    .flatMapGhost()
                    .map { request -> Output.RequestScreenCapture(request) },
                featureFlagRepo.observe()
                    .map { features -> Output.SettingsUpdated(features) }
            )
        )
    }

    private val mapInputToUsecase: (Observable<MainView.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(MainView.Input.MediaProjectionUpdated::class.java)
                        .flatMapCompletable { updateMediaProjectionUC.execute(it.mediaProjection) },
                    input.ofType(MainView.Input.CheckAllPermisionsOnStartup::class.java)
                        .flatMapCompletable { checkAllPermissionsUC.execute() },
                )
            )
        }
}
