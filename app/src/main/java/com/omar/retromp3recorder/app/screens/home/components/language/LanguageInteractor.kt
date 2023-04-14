package com.omar.retromp3recorder.app.screens.home.components.language

import com.omar.retromp3recorder.app.Interactor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageInteractor @Inject constructor(
    private val languageVisibilityMapper: LanguageVisibilityMapper,
    dispatcher: CoroutineDispatcher,
) : Interactor<LanguageContract.Input, LanguageContract.Output>(dispatcher) {
    override fun listRepos(): List<Flow<LanguageContract.Output>> {
        return listOf(languageVisibilityMapper.execute()
            .map { LanguageContract.Output.Visibility(it) })
    }

    override suspend fun ProducerScope<LanguageContract.Output>.launchUseCase(input: LanguageContract.Input) {
//        TODO("Not yet implemented")
    }
}
