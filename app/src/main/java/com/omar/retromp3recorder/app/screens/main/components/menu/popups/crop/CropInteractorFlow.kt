package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.bl.actions.CropUC
import com.omar.retromp3recorder.bl.crop.CropInPlaceUC
import com.omar.retromp3recorder.bl.crop.GenerateFileNameUC
import com.omar.retromp3recorder.bl.files.CanSaveAsNameUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.domain.Optional
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CropInteractorFlow @Inject constructor(
    private val canSaveAs: CanSaveAsNameUC,
    private val cropInPlaceUC: CropInPlaceUC,
    private val cropOutsideUC: CropUC,
    private val nameGenerator: GenerateFileNameUC,
    private val toastRepo: ToastRepo,
    dispatcher: CoroutineDispatcher
) : Interactor<CropContract.Input, CropContract.Output>(dispatcher) {

    private val dismissBus = MutableSharedFlow<Boolean>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )

    private suspend fun emitOnCropResult(result: Optional<ExistingFileWrapper>) {
        val toast = if (result.hasValue().not()) Stringer(R.string.toast_crop_failed) else {
            Stringer(R.string.toast_crop_success)
        }
        toastRepo.emit(toast)
        dismissBus.emit(true)
    }

    override fun listRepos(): List<Flow<CropContract.Output>> {
        return listOf(
            flow {
                emit(CropContract.Output.FileNameUpdate(nameGenerator.execute()))
                emit(CropContract.Output.IsActionEnabled(true))
            },
            dismissBus.map { CropContract.Output.Dismiss }
        )
    }

    override suspend fun FlowCollector<CropContract.Output>.launchUseCase(input: CropContract.Input) {
        when (input) {
            is CropContract.Input.CheckCanCrop -> {
                val canRename = canSaveAs.execute(input.nameSuggestion.path)
                emit(CropContract.Output.IsActionEnabled(canRename))
            }
            is CropContract.Input.CropInPlace -> {
                emit(CropContract.Output.Loading)
                val result = cropInPlaceUC.execute(input.nameSuggestion)
                emitOnCropResult(result)
            }
            is CropContract.Input.CropOutside -> {
                emit(CropContract.Output.Loading)
                val result = cropOutsideUC.execute(input.nameSuggestion)
                emitOnCropResult(result)
            }
        }
    }
}
