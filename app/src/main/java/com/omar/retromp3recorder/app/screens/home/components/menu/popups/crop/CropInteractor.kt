package com.omar.retromp3recorder.app.screens.home.components.menu.popups.crop

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.bl.actions.BuyCrop10UC
import com.omar.retromp3recorder.bl.actions.CropWithProductUC
import com.omar.retromp3recorder.bl.actions.HasCropPurchaseUC
import com.omar.retromp3recorder.bl.crop.CropInPlaceUC
import com.omar.retromp3recorder.bl.crop.GenerateFileNameUC
import com.omar.retromp3recorder.bl.files.CanSaveAsNameUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.domain.repo.PublishSubjectRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CropInteractor @Inject constructor(
    private val canSaveAs: CanSaveAsNameUC,
    private val cropInPlaceUC: CropInPlaceUC,
    private val cropOutsideUC: CropWithProductUC,
    private val hasCropPurchaseUC: HasCropPurchaseUC,
    private val buyCrop10UC: BuyCrop10UC,
    private val nameGenerator: GenerateFileNameUC,
    private val toastRepo: ToastRepo,
    dispatcher: CoroutineDispatcher
) : Interactor<CropContract.Input, CropContract.Output>(dispatcher) {

    private val outputBus = PublishSubjectRepo<CropContract.Output>()

    private suspend fun emitOnCropResult(result: Result<ExistingFileWrapper>) {
        val toast = Stringer(
            if (result.isFailure) R.string.toast_crop_failed
            else R.string.toast_crop_success
        )
        toastRepo.emit(toast)
        outputBus.emit(CropContract.Output.Dismiss)
    }

    override fun listRepos(): List<Flow<CropContract.Output>> {
        return listOf(
            outputBus,
            flow {
                val hasCropPurchase = hasCropPurchaseUC.execute()
                if (!hasCropPurchase) {
                    emit(CropContract.Output.Dismiss)
                    buyCrop10UC.execute()
                } else {
                    emit(CropContract.Output.Show)
                    emit(CropContract.Output.FileNameUpdate(nameGenerator.execute()))
                    emit(CropContract.Output.IsActionEnabled(true))
                }
            },
        )
    }

   override suspend fun launchUseCase(input: CropContract.Input) {
        when (input) {
            is CropContract.Input.CheckCanCrop -> {
                val canRename = canSaveAs.execute(input.nameSuggestion.path)
                outputBus.emit(CropContract.Output.IsActionEnabled(canRename))
            }
            is CropContract.Input.CropInPlace -> {
                outputBus.emit(CropContract.Output.Loading)
                val result = cropInPlaceUC.execute(input.nameSuggestion)
                emitOnCropResult(result)
            }
            is CropContract.Input.CropOutside -> {
                outputBus.emit(CropContract.Output.Loading)
                val result = cropOutsideUC.execute(input.nameSuggestion)
                emitOnCropResult(result)
            }
        }
    }
}
