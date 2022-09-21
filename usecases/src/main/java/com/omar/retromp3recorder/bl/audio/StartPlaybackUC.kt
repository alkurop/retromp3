package com.omar.retromp3recorder.bl.audio

import android.Manifest
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.PlayerStartOptions
import com.omar.retromp3recorder.bl.system.CheckPermissionsUC
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.FromToMillis
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.PermissionsRequestBus
import com.omar.retromp3recorder.storage.repo.PermissionsRequestBus.ShouldRequestPermissions
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import com.omar.retromp3recorder.utils.takeOne
import com.omar.retromp3recorder.utils.toFromToMillis
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class StartPlaybackUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val currentFileRepo: CurrentFileRepo,
    private val permissionsRequestBus: PermissionsRequestBus,
    private val playerProgressRepo: PlayerProgressRepo,
) {
    fun execute(): Completable {
        val abort = Completable.complete()
        val execute =
            Observable.combineLatest(
                currentFileRepo.observe(),
                playerProgressRepo.observe()
            ) { p1, p2 -> Pair(p1, p2) }
                .takeOne()
                .flatMapCompletable { (file, progressState) ->
                    Completable.fromAction {
                        val existingFile = file.value as ExistingFileWrapper
                        val progress = progressState.value!!
                        val fromToMillis = if (progress.range.settings.isActive) {
                            progress.range.toFromToMillis(
                                existingFile.length!!
                            )
                        } else {
                            FromToMillis(
                                from = progress.progress,
                                to = existingFile.length!!
                            )
                        }
                        val relativeSeekPosition =
                            (progress.progress - fromToMillis.from).coerceAtLeast(
                                0L
                            )
                        val seekPosition =
                            if (progress.progress > fromToMillis.to) 0 else relativeSeekPosition

                        val options = PlayerStartOptions(
                            filePath = existingFile.path,
                            rangeMillis = fromToMillis,
                            length = existingFile.length!!,
                            relativeSeekPosition = seekPosition
                        )
                        audioPlayer.onInput(
                            AudioPlayer.Input.Start(
                                options
                            )
                        )
                    }
                }
        return checkPermissionsUC.execute(playbackPermissions)
            .andThen(permissionsRequestBus.observe().takeOne())
            .flatMapCompletable { shouldAskPermissions ->
                if (shouldAskPermissions is ShouldRequestPermissions.Granted) execute
                else abort
            }
    }

    private val playbackPermissions: Set<String> = setOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}
