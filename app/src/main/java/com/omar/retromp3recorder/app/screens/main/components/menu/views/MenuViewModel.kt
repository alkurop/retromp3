package com.omar.retromp3recorder.app.screens.main.components.menu.views

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.MenuInteractor
import com.omar.retromp3recorder.utils.domain.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class MenuViewModel : ViewModel() {
    @Inject
    lateinit var interactor: MenuInteractor

    private val compositeDisposable = CompositeDisposable()

    val state = BehaviorSubject.create<MenuContract.State>()
    val input = PublishSubject.create<MenuContract.Input>()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .subscribe(state::onNext) { Timber.e(it) }
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
