package com.omar.retromp3recorder.app.ui.menu.container.logic

import com.omar.retromp3recorder.bl.enablers.EnablersSwitcher
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import com.omar.retromp3recorder.utils.processIO
import com.omar.retromp3recorder.utils.toOptional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class MenuInteractor @Inject constructor(
    private val menuPopupBus: MenuPopupBus,
    private val menuStateExcavator: MenuStateExcavator,
    private val eneblersSwitcher: EnablersSwitcher,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<MenuView.Input, MenuView.State> =
        scheduler.processIO(inputMapper, stateMapper)

    private val stateMapper: () -> Observable<MenuView.State> = { menuStateExcavator.observe() }

    private val inputMapper: (Observable<MenuView.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(MenuView.Input.Enable::class.java).flatMapCompletable {
                        eneblersSwitcher.execute(it.enabler, it.isEnabled)
                    },
                    input.ofType(MenuView.Input.Popup::class.java).flatMapCompletable {
                        Completable.fromAction {
                            menuPopupBus.onNext(it.action.toOptional())
                        }
                    },
                )
            )
        }
}

