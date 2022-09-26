package com.omar.retromp3recorder.bl.system

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.storage.repo.global.PermissionsRequestBus
import com.omar.retromp3recorder.storage.repo.global.PermissionsRequestBus.ShouldRequestPermissions.Denied
import com.omar.retromp3recorder.utils.PermissionCheckerUtil
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CheckPermissionsUC @Inject constructor(
    private val permissionChecker: PermissionCheckerUtil,
    private val permissionsRequestBus: PermissionsRequestBus
) {
    fun execute(permissions: Set<String>): Completable {
        return Observable
            .fromCallable {
                val uncheckedPermissions = permissionChecker.showUnchecked(permissions)
                if (uncheckedPermissions.isEmpty()) PermissionsRequestBus.ShouldRequestPermissions.Granted else Denied(
                    Shell(uncheckedPermissions)
                )
            }
            .flatMapCompletable { shouldRequestPermissions ->
                Completable.fromAction { permissionsRequestBus.onNext(shouldRequestPermissions) }
            }
    }
}
