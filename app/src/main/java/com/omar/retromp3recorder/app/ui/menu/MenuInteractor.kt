package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.bl.actions.AudioActionsExecutor
import com.omar.retromp3recorder.bl.enablers.EnablersReverseSwitcher
import com.omar.retromp3recorder.dto.MenuAction
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class MenuInteractor @Inject constructor(
    private val audioActionsExecutor: AudioActionsExecutor,
    private val menuStateMapper: MenuStateMapper,
    private val reverseEnablersSwitcher: EnablersReverseSwitcher,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<MenuAction, MenuView.State> =
        scheduler.processIO(inputMapper, stateMapper)

    private val stateMapper: () -> Observable<MenuView.State> =
        { menuStateMapper.observe() }
    private val inputMapper: (Observable<MenuAction>) -> Completable =
        { input ->
            Completable.merge(listOf(
                input.ofType(MenuAction.Enable::class.java).flatMapCompletable {
                    reverseEnablersSwitcher.execute(it)
                },
                input.ofType(MenuAction.Execute::class.java).flatMapCompletable {
                    audioActionsExecutor.execute(it.menuExecutable)
                },
            ))
        }
}
