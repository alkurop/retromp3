package com.omar.retromp3recorder.app.ui.menu

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.bl.enablers.EnablersReverseSwitcher
import com.omar.retromp3recorder.dto.MenuAction
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class MenuInteractor @Inject constructor(
    private val menuPopupBus: MenuPopupBus,
    private val menuStateExcavator: MenuStateExcavator,
    private val reverseEnablersSwitcher: EnablersReverseSwitcher,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<MenuAction, MenuView.State> =
        scheduler.processIO(inputMapper, stateMapper)

    private val stateMapper: () -> Observable<MenuView.State> =
        //todo Need of view scope is becoming critical
        { menuStateExcavator.observe() }

    private val inputMapper: (Observable<MenuAction>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(MenuAction.Enable::class.java).flatMapCompletable {
                        reverseEnablersSwitcher.execute(it)
                    },
                    input.ofType(MenuAction.Popup::class.java).flatMapCompletable {
                        Completable.fromAction { menuPopupBus.onNext(Shell(it.menuExecutable)) }
                    },
                )
            )
        }
}

