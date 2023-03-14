package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.bl.crop.CropFileNameUpdater
import com.omar.retromp3recorder.bl.crop.CropInPlaceUC
import com.omar.retromp3recorder.bl.files.CanSaveAsNameUC
import com.omar.retromp3recorder.domain.NewNameSuggestion
import com.omar.retromp3recorder.storage.repo.common.StateFlowRepo
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CropInteractorFlow @Inject constructor(
    private val canSaveAs: CanSaveAsNameUC,
    private val cropInPlaceUC: CropInPlaceUC,
    private val cropOutsideUC: CropUC,
    private val nameUpdater: CropFileNameUpdater,
    private val toastRepo: ToastRepo,
    dispatcher: CoroutineDispatcher
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher
    private val canCropFileRepo = StateFlowRepo(false)
    private val nameSuggestionRepo = StateFlowRepo(NewNameSuggestion())
    private val dismissBus = MutableSharedFlow<Boolean>()

    fun processIO(upstream: Flow<CropContract.Input>): Flow<CropContract.Output> {
        return listOf(
            upstream.processInputs(),
            listenToRepos()
        ).merge().distinctUntilChanged()
    }

    private fun Flow<CropContract.Input>.processInputs(): Flow<CropContract.Output> {
        return this.transform { input ->
            launch {
                when (input) {
                    is CropContract.Input.CheckCanCrop -> {
                        nameSuggestionRepo.emit(input.nameSuggestion)
                        val canRename = canSaveAs.execute(input.nameSuggestion.path)
                        emit(CropContract.Output.IsActionEnabled(canRename))
                    }
                    is CropContract.Input.CropInPlace -> {
                        cropInPlaceUC.execute(input.nameSuggestion)
                        dismissBus.emit(true)
                    }
                    is CropContract.Input.CropOutside -> {
                        val result = cropOutsideUC.execute(input.nameSuggestion)
                        val toast =
                            if (result.value == null) Stringer(R.string.toast_crop_failed) else {
                                Stringer(R.string.toast_crop_success)
                            }
                        toastRepo.onNext(toast)
                        dismissBus.emit(true)
                    }
                }
            }
        }
    }

    private fun listenToRepos(): Flow<CropContract.Output> {
        return listOf(
            dismissBus.map { CropContract.Output.Dismiss },
            canCropFileRepo.flow().map {
                CropContract.Output.IsActionEnabled(it)
            },
            nameUpdater.flow().map {
                val nameSuggestion =
                    it.value?.apply { canCropFileRepo.tryNext(true) } ?: NewNameSuggestion()
                nameSuggestionRepo.tryNext(nameSuggestion)
                CropContract.Output.FileNameUpdate(nameSuggestion)
            },
            nameSuggestionRepo.flow().map {
                CropContract.Output.FileNameUpdate(it)
            }
        ).merge()
    }
}
