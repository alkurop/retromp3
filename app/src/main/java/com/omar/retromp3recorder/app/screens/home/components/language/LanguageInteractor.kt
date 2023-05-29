package com.omar.retromp3recorder.app.screens.home.components.language

import com.omar.retromp3recorder.app.Interactor
import com.omar.retromp3recorder.domain.LanguageState
import com.omar.retromp3recorder.storage.repo.global.LanguageAvailabilityRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageInteractor @Inject constructor(
    private val languageVisibilityMapper: LanguageVisibilityMapper,
    private val languageAvailabilityRepo: LanguageAvailabilityRepo,
    dispatcher: CoroutineDispatcher,
) : Interactor<LanguageContract.Input, LanguageContract.Output>(dispatcher) {
    override fun listRepos(): List<Flow<LanguageContract.Output>> {
        return listOf(
            languageVisibilityMapper.execute()
                .map { LanguageContract.Output.Visibility(it) },
            languageAvailabilityRepo.flow().map { languages -> LanguageContract.Output.Availability(
                languages.filter { it.state is LanguageState.Available }.map { it.language }
            ) }
        )
    }

    override suspend fun launchUseCase(input: LanguageContract.Input) {
//        TODO("Not yet implemented")
    }
}
