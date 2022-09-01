package com.omar.retromp3recorder.bl.audio

import android.Manifest
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.PlayerStartOptions
import com.omar.retromp3recorder.bl.system.CheckPermissionsUC
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.PermissionsRequestBus
import com.omar.retromp3recorder.storage.repo.PermissionsRequestBus.ShouldRequestPermissions
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import com.omar.retromp3recorder.utils.takeOneObservable
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
                playerProgressRepo.observe(), { p1, p2 -> Pair(p1, p2) })
                .takeOneObservable()
                .flatMapCompletable { (fileName, progressState) ->
                    Completable.fromAction {
                        audioPlayer.onInput(
                            AudioPlayer.Input.Start(
                                PlayerStartOptions(
                                    filePath = fileName.value!!,
                                    seekPosition = progressState.value?.progress
                                )
                            )
                        )
                    }
                }
        return checkPermissionsUC.execute(playbackPermissions)
            .andThen(permissionsRequestBus.observe().takeOneObservable())
            .flatMapCompletable { shouldAskPermissions ->
                if (shouldAskPermissions is ShouldRequestPermissions.Granted) execute
                else abort
            }
    }

    private val playbackPermissions: Set<String> = setOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}
