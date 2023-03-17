package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.bl.crop.CropInPlaceUC
import com.omar.retromp3recorder.bl.crop.GenerateFileNameUC
import com.omar.retromp3recorder.bl.files.CanSaveAsNameUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.platform.Optional
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class CropInteractorFlow @Inject constructor(
    private val canSaveAs: CanSaveAsNameUC,
    private val cropInPlaceUC: CropInPlaceUC,
    private val cropOutsideUC: CropUC,
    private val nameGenerator: GenerateFileNameUC,
    private val toastRepo: ToastRepo,
    private val dispatcher: CoroutineDispatcher
) {
    private val dismissBus = MutableSharedFlow<Boolean>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )

    fun processIO(upstream: Flow<CropContract.Input>): Flow<CropContract.Output> {
        return listOf(
            listenToRepos(),
            flow {
                emit(CropContract.Output.FileNameUpdate(nameGenerator.execute()))
            },
            upstream.processInputs(),
        ).merge().flowOn(dispatcher)
    }

    private fun Flow<CropContract.Input>.processInputs(): Flow<CropContract.Output> {
        return this.flatMapMerge { input ->
            flow {
                when (input) {
                    is CropContract.Input.CheckCanCrop -> {
                        val canRename = canSaveAs.execute(input.nameSuggestion.path)
                        emit(CropContract.Output.IsActionEnabled(canRename))
                    }
                    is CropContract.Input.CropInPlace -> {
                        val result = cropInPlaceUC.execute(input.nameSuggestion)
                        emitOnCropResult(result)
                    }
                    is CropContract.Input.CropOutside -> {
                        val result = cropOutsideUC.execute(input.nameSuggestion)
                        emitOnCropResult(result)
                    }
                }
            }
        }
    }

    private suspend fun emitOnCropResult(result: Optional<ExistingFileWrapper>) {
        val toast =
            if (result.hasValue()) Stringer(R.string.toast_crop_failed) else {
                Stringer(R.string.toast_crop_success)
            }
        toastRepo.emit(toast)
        dismissBus.emit(true)
    }

    private fun listenToRepos(): Flow<CropContract.Output> {
        return listOf(
            dismissBus.map { CropContract.Output.Dismiss }
        ).merge()
    }
}
