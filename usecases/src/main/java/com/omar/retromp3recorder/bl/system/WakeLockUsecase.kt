package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.utils.ServiceDealer
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class WakeLockUsecase @Inject constructor(
    private val serviceDealer: ServiceDealer
) {
    fun execute(): Completable =
        Completable.fromAction {
            serviceDealer.startWakelockService()
        }
}
