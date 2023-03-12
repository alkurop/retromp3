package com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete

import com.omar.retromp3recorder.bl.files.DeleteCurrentFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.mapToUsecase
import com.omar.retromp3recorder.utils.domain.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class DeleteFileInteractor @Inject constructor(
    private val deleteCurrentFileUC: DeleteCurrentFileUC,
    private val currentFileMapper: CurrentFileRepo,
    private val scheduler: Scheduler
) {
    private val shouldDismiss = PublishSubject.create<Boolean>()
    fun processIO(): ObservableTransformer<DeleteFileContract.Input, DeleteFileContract.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<DeleteFileContract.Output> = {
        Observable.merge(
            listOf(
                shouldDismiss.map { DeleteFileContract.Output.Dismiss },
                currentFileMapper.observe().map {
                    DeleteFileContract.Output.CurrentFile(
                        it.value as? ExistingFileWrapper
                    )
                },
            )
        )
    }
    private val mapInputToUsecase: (Observable<DeleteFileContract.Input>) -> Completable =
        { input ->
            Completable.merge(

                listOf(
                    input.mapToUsecase<DeleteFileContract.Input.DeleteFile> {
                        deleteCurrentFileUC
                            .execute()
                            .andThen(Completable
                                .fromAction { shouldDismiss.onNext(false) }
                            )
                    }
                )
            )
        }
}


