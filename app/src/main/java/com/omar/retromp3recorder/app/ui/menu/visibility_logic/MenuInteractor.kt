package com.omar.retromp3recorder.app.ui.menu.visibility_logic

import com.omar.retromp3recorder.app.ui.menu.MenuContract
import com.omar.retromp3recorder.bl.enablers.EnablersSwitcher
import com.omar.retromp3recorder.utils.domain.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class MenuInteractor @Inject constructor(
    private val menuStateExcavator: MenuStateExcavator,
    private val eneblersSwitcher: EnablersSwitcher,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<MenuContract.Input, MenuContract.State> =
        scheduler.processIO(inputMapper, stateMapper)

    private val stateMapper: () -> Observable<MenuContract.State> = { menuStateExcavator.observe() }

    private val inputMapper: (Observable<MenuContract.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(MenuContract.Input.Enable::class.java).flatMapCompletable {
                        eneblersSwitcher.execute(it.enabler, it.isEnabled)
                    },
                )
            )
        }
}

