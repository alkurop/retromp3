package com.omar.retromp3recorder.app.screens.search

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.bl.files.SetCurrentFileUC
import com.omar.retromp3recorder.domain.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.DatabasePagingProvider
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.utils.domain.repo.PublishSubjectRepo
import com.omar.retromp3recorder.utils.domain.toLoadingState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SelectorInteractor @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val pagingProvider: DatabasePagingProvider,
    private val setCurrentFileUC: SetCurrentFileUC,
    dispatcher: CoroutineDispatcher
) : Interactor<SelectorContract.Input, SelectorContract.Output>(dispatcher) {
    private val outputBus = PublishSubjectRepo<SelectorContract.Output>()
    override fun listRepos(): List<Flow<SelectorContract.Output>> {
        return listOf(
            flow {
                emit(currentFileRepo.first().run {
                    SelectorContract.Output.CurrentFile(value as? ExistingFileWrapper)
                })
                emit(
                    SelectorContract.Output.CurrentFlow(
                        pagingProvider.createFlow("").toLoadingState()
                    )
                )
            },
            outputBus.flow()
        )
    }

    override suspend fun launchUseCase(input: SelectorContract.Input) {
        when (input) {
            is SelectorContract.Input.ItemSelected -> setCurrentFileUC.execute(input.item)
            is SelectorContract.Input.SetQuery -> {
                outputBus.emit(
                    SelectorContract.Output.CurrentFlow(
                        pagingProvider.createFlow(input.query).toLoadingState()
                    )
                )
            }
        }
    }
}

