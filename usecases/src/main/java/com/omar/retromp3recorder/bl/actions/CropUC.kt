package com.omar.retromp3recorder.bl.actions

import com.omar.retromp3recorder.bl.files.GetCropFileNameUC
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.io.audiotransformer.AudioCropper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.JoinedProgressRepo
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


//todo disable menu during recording and hide when no file present
class CropUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val audioCropper: AudioCropper,
    private val currentFileRepo: CurrentFileRepo,
    private val getCropFileNameUC: GetCropFileNameUC,
    private val joinedProgressRepo: JoinedProgressRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable =
        Single.zip(
            joinedProgressRepo.takeOne(),
            currentFileRepo.takeOne()
        ) { progress, optional ->
            val range = (progress as JoinedProgress.PlayerProgressShown).progress.range

            val fileWrapper = (optional.value!! as ExistingFileWrapper)
            val currentPath = fileWrapper.path
            getCropFileNameUC.execute(currentPath).map { currentPath to it }
        }.flatMapCompletable {
            Completable.complete()
//            audioCropper.crop()
        }
}
