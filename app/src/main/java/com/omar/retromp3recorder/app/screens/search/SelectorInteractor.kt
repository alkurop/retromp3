package com.omar.retromp3recorder.app.screens.search

import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.processIO
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
    fun processIO(): ObservableTransformer<SelectorContract.Input, SelectorContract.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )


    private val mapRepoToOutput: () -> Observable<SelectorContract.Output> = {
        Observable.merge(
            listOf(
                currentFileRepo.observe().map {
                    SelectorContract.Output.CurrentFile(it.value!!.path)
                },
                Observable.just(pagingProvider.provideItemSource()).map {
                    SelectorContract.Output.FileListNew(itemsSource = it)
                },
            )
        )
    }
    private val mapInputToUsecase: (Observable<SelectorContract.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(SelectorContract.Input.ItemSelected::class.java)
                        .flatMapCompletable {
                            setCurrentFileUC.execute(it.item)
                        },
                )
            )
        }
}
