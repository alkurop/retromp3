package com.omar.retromp3recorder.app.ui.files.selector

import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class SelectorInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val currentFileRepo: CurrentFileRepo,
    private val pagingProvider: DatabasePagingProvider,
    private val setCurrentFileUC: SetCurrentFileUC
) {
    fun processIO(): ObservableTransformer<SelectorView.Input, SelectorView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<SelectorView.Output> = {
        Observable.merge(
            listOf(
                currentFileRepo.observe().map {
                    SelectorView.Output.CurrentFile(it.value!!.path)
                },
                Observable.just(pagingProvider.providePagingFiles()).map {
                    SelectorView.Output.FileList(it)
                },
                Observable.just(pagingProvider.provideItemSource()).map {
                    SelectorView.Output.FileListNew(itemsSource = it)
                }
            )
        )
    }
    private val mapInputToUsecase: (Observable<SelectorView.Input>) -> Completable =
        { input ->
            Completable.merge(listOf(
                input.ofType(SelectorView.Input.ItemSelected::class.java)
                    .flatMapCompletable {
                        setCurrentFileUC.execute(it.item)
                    }
            ))
        }
}
