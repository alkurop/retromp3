package com.omar.retromp3recorder.storage.repo.local

import com.omar.retromp3recorder.domain.PlayerProgress
import com.omar.retromp3recorder.domain.PlayerRange
import com.omar.retromp3recorder.storage.repo.common.ReducerRepo
import com.omar.retromp3recorder.utils.platform.Optional
import com.omar.retromp3recorder.utils.platform.ScopeJobWrapper
import com.omar.retromp3recorder.utils.platform.toOptional
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerProgressRepo @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
    private val progressMapper: PlayerProgressMapperFlow,
    private val dispatcher: CoroutineDispatcher,
    jobWrapper: ScopeJobWrapper
) : ReducerRepo<PlayerProgressRepo.In, Optional<PlayerProgress>>(
    init = Optional.empty(),
    reducer = reducer
) {

    init {
        jobWrapper.launch {
            progressMapper.execute().flowOn(dispatcher).collect {
                emit(In.Progress(it))
            }
        }
    }

    sealed class In {
        data class NewCurrentFile(val progress: PlayerProgress) : In()
        data class Range(val range: PlayerRange) : In()
        data class Progress(val progress: PlayerProgress) : In()
        data class Seek(val progress: Long) : In()
        object Hidden : In()
    }

    override fun flow(): Flow<Optional<PlayerProgress>> {
        return combine(
            super.flow(),
            playerControlsRepo.flow()
        ) { progress, controls ->
            progress.value?.let {
                it.copy(range = it.range.copy(settings = controls.rangeSettings))
            }.toOptional()
        }
    }

    private companion object {
        val reducer: Optional<PlayerProgress>.(In) -> Optional<PlayerProgress> =
            { input ->
                when (input) {
                    is In.NewCurrentFile -> input.progress
                    is In.Range -> this.value?.copy(range = input.range)
                    is In.Progress -> {
                        val range = this.value?.range ?: PlayerRange()
                        input.progress.copy(range = range)
                    }
                    is In.Seek -> requireNotNull(this.value) { "Range is empty while seeking" }
                        .copy(progress = input.progress)
                    else -> null
                }.toOptional()
            }
    }
}
