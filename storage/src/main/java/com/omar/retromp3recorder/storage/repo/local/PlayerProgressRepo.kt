package com.omar.retromp3recorder.storage.repo.local

import com.omar.retromp3recorder.dto.PlayerProgress
import com.omar.retromp3recorder.dto.PlayerRange
import com.omar.retromp3recorder.dto.Track
import com.omar.retromp3recorder.storage.repo.common.ReducerRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@Track
class PlayerProgressRepo @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo
) :
    ReducerRepo<PlayerProgressRepo.In, Optional<PlayerProgress>>(
        init = Optional.empty(),
        function = FUNCTION
    ) {
    sealed class In {
        data class Seek(
            val progress: Long,
        ) : In()

        data class Progress(val progress: PlayerProgress) : In()
        data class Range(val range: PlayerRange) : In()
        data class NewCurrentFile(val progress: PlayerProgress) : In()

        object Hidden : In()
    }

    override fun observe(): Observable<Optional<PlayerProgress>> {
        return Observable.combineLatest(
            super.observe(),
            playerControlsRepo.observe()
        ) { progress, features ->
            Optional(progress.value?.let {
                it.copy(
                    range = it.range.copy(
                        settings = features.range
                    )
                )
            })
        }
    }
}

private val FUNCTION: Optional<PlayerProgress>.(PlayerProgressRepo.In) -> Optional<PlayerProgress> =
    { input ->
        when (input) {
            is PlayerProgressRepo.In.Seek -> Optional(
                this.value!!.copy(
                    progress = input.progress,
                )
            )
            is PlayerProgressRepo.In.Progress -> {
                val range = this.value?.range ?: PlayerRange()
                Optional(input.progress.copy(range = range))
            }
            is PlayerProgressRepo.In.NewCurrentFile -> Optional(input.progress)
            is PlayerProgressRepo.In.Range -> Optional(this.value?.copy(range = input.range))

            else -> Optional.empty()
        }
    }
