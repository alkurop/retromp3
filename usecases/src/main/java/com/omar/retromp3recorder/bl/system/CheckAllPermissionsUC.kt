package com.omar.retromp3recorder.bl.system

import android.Manifest
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CheckAllPermissionsUC @Inject constructor(
    private val checkPermissionsUC: CheckPermissionsUC,
) {
    fun execute(): Completable {
        return checkPermissionsUC.execute(playbackPermissions)
    }
}

private val playbackPermissions: Set<String> = setOf(
    Manifest.permission.RECORD_AUDIO
)
